package nl.bos;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.BoundingBox;
import com.microsoft.playwright.options.LoadState;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

class AppworksTipsRegressionTests {
    private static final double UI_TIMEOUT_MILLIS = 30_000;

    @Test
    void givenAppworksTipsUrl_whenOpenHomePage_thenPageTitleIsShown() {
        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true))) {
            Page page = browser.newPage();

            page.navigate("https://appworks-tips.com");

            assertThat(page).hasTitle("OpenText™ Process Automation Tips | Installments, thoughts, tricks and ideas");
        }
    }

    @Test
    void givenValidOtdsCredentials_whenSignInAndSignOut_thenLogoutConfirmationIsShown() {
        String loginUrl = "http://192.168.56.102:8181/otdsws/login";

        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions().setHeadless(true);
        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.chromium().launch(launchOptions);
             BrowserContext context = browser.newContext(new Browser.NewContextOptions().setIgnoreHTTPSErrors(true))) {
            Page page = context.newPage();

            page.navigate(loginUrl);
            page.locator("#otds_username").fill("opadev"); //Read this from an environment variable!
            page.locator("#otds_password").fill("admin"); //Read this from an environment variable!
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign in")).click();

            assertThat(page.getByText("Logged in as")).isVisible();

            page.getByText("Sign out").click();

            assertThat(page.getByText("You have been logged out.")).isVisible();
        }
    }

    @Test
    void givenAuthenticatedUser_whenCreateAndDeleteCaseFromRuntimeUi_thenCaseIsRemovedAndUserCanSignOut() {
        String loginUrl = "http://192.168.56.102:8181/otdsws/login";
        String runtimeUrl = "http://192.168.56.102:8080/home/opa_tips/app/start/web";
        String caseName = "test" + System.currentTimeMillis();

        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions().setHeadless(true);
        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.chromium().launch(launchOptions);
            BrowserContext context = browser.newContext(new Browser.NewContextOptions().setIgnoreHTTPSErrors(true))) {
            Page page = context.newPage();

            // Login with credentials against OTDS; AND validate 'Logged in as'
            page.navigate(loginUrl);
            page.locator("#otds_username").fill("opadev"); //Read this from an environment variable!
            page.locator("#otds_password").fill("admin"); //Read this from an environment variable!
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign in")).click();
            assertThat(page.getByText("Logged in as")).isVisible();

            // Forward to the runtime UI; AND validate the 'Create' icon
            page.navigate(runtimeUrl);
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
            Locator createMenu = page.locator("createables-menu")
                    .getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Create a new item"));
            assertThat(createMenu).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(UI_TIMEOUT_MILLIS));

            // Click the 'Create' menu (top-right icon); AND validate the 'All items' menu-item
            createMenu.click();
            Locator createablesMenu = page.locator("createables-menu");
            Locator allItemsMenuItem = createablesMenu.locator("#all-header")
                    .filter(new Locator.FilterOptions().setVisible(true));
            assertThat(allItemsMenuItem).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(UI_TIMEOUT_MILLIS));

            // Click on 'All items'; AND validate the 'Case' menu-item
            allItemsMenuItem.click();
            Locator caseMenuItem = createablesMenu.locator("#all-group")
                    .getByRole(AriaRole.MENUITEM, new Locator.GetByRoleOptions().setName("Case"))
                    .filter(new Locator.FilterOptions().setVisible(true));
            assertThat(caseMenuItem).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(UI_TIMEOUT_MILLIS));

            // Click on 'Case'; AND validate the modal popup
            caseMenuItem.click();
            Locator createCaseDialog = page.locator("ux-dialog-container.active");
            assertThat(createCaseDialog).isVisible();

            // Fillin a name for the case; AND validate the 'Create' button
            Locator caseNameInput = createCaseDialog.getByLabel("Name");
            caseNameInput.click();
            caseNameInput.pressSequentially(caseName);
            assertThat(caseNameInput).hasValue(caseName);
            caseNameInput.press("Tab");
            Locator createButton = createCaseDialog.locator("bs-drop-button")
                    .filter(new Locator.FilterOptions().setHasText("Create"))
                    .locator(".drop-button");
            assertThat(createButton).isVisible();

            // Click the 'Create' button; AND validate the instance in the list
            Locator createButtonLabel = createButton.locator(".ot-drop-button-label");
            createButtonLabel.scrollIntoViewIfNeeded();
            BoundingBox createButtonBox = createButtonLabel.boundingBox();
            page.mouse().click(
                    createButtonBox.x + createButtonBox.width / 2,
                    createButtonBox.y + createButtonBox.height / 2
            );
            assertThat(createCaseDialog).isHidden();
            Locator createdCase = page.getByText(caseName)
                    .filter(new Locator.FilterOptions().setVisible(true))
                    .first();
            assertThat(createdCase).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(UI_TIMEOUT_MILLIS));

            // Select the created instance; AND validate the 'Delete' button in the action bar
            createdCase.click();
            Locator deleteButton = page.locator("button[aria-label='Delete']")
                    .filter(new Locator.FilterOptions().setVisible(true))
                    .first();
            assertThat(deleteButton).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(UI_TIMEOUT_MILLIS));

            // Click the 'Delete' button; AND confirm the deletion in the modal popup
            deleteButton.click();
            Locator deleteCaseDialog = page.locator("ux-dialog-container.active");
            Locator confirmDeleteButton = deleteCaseDialog.getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Delete"))
                    .filter(new Locator.FilterOptions().setVisible(true))
                    .first();
            assertThat(confirmDeleteButton).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(UI_TIMEOUT_MILLIS));
            confirmDeleteButton.click();
            assertThat(createdCase).isHidden(new LocatorAssertions.IsHiddenOptions().setTimeout(UI_TIMEOUT_MILLIS));

            // Click the 'Profile' menu (top-right icon); AND validate the 'Sign out' menu-item
            Locator profileMenu = page.locator("[aria-label*='Profile'], [aria-label*='profile'], [aria-label*='User'], [aria-label*='user']").first();
            assertThat(profileMenu).isVisible();
            profileMenu.click();
            Locator signOutMenuItem = page.getByText("Sign out");
            assertThat(signOutMenuItem).isVisible();

            // Click 'Sign out'; AND validate the OTDS login screen
            signOutMenuItem.click();
            assertThat(page.locator("#otds_username")).isVisible();
        }
    }
}
