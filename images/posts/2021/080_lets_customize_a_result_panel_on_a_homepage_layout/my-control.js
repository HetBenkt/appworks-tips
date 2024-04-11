const template = document.createElement('template');
template.innerHTML = `
<style>
    :host {
    display: block;
    }
    .project-rows-container {
      display: table;        
      width: auto;         
      background-color: #eee;         
      border: 2px solid #666666;         
      border-spacing: 5px; /* cellspacing:poor IE support for  this */
    }
    .project-row-entry {
      display: table-row;
      width: auto;
      clear: both;
    }
    .project-row-cell {
      float: left; /* fix for  buggy browsers */
      display: table-column;      
      border: 1px solid #666666;         
      width: 250px; 
      background-color: #DDD;
      padding: 5px;
    }
    .project-create {
      margin-bottom: 10px;
      margin-right: 10px;
    }
</style>
<div>
   <H1>List of projects</H1>
   <div>
      <button class="project-create" title="Create">Create</button>
      <button class="project-export" title="Export">Export</button>
   </div>
   <div style="width:100%" class="project-row-content-container">
		<div class="project-rows-container">
		</div>
   </div>
</div>
`;

class MyControl extends HTMLElement {
    /**
     * @method exportResults
     * Export results to csv or xlsx
     */
    get exportResults() {
        return this._exportResults;
    }

    set exportResults(newValue) {
        this._exportResults = newValue
    }

    /**
     * @method openItem
     * Open item function passed in from custom-list-element
     */
    get openItem() {
        return this._openItem;
    }

    set openItem(newValue) {
        this._openItem = newValue
    }

    /**
     * @method triggerUpdate
     * If process platform client believes there is a need to refresh results this be called
     */
    get triggerUpdate() {
        return this._triggerUpdate;
    }

    set triggerUpdate(newValue) {
        this._triggerUpdate = newValue
        if (newValue) {
            this.getGridResults().then((function (result) {
                this.generateView(result);
            }).bind(this));
        }
    }

    /**
     * @method settings
     * The xml settings defined in CWS
     */
    get settings() {
        return this._settings;
    }

    set settings(newValue) {
        this._settings = newValue;
        this.projNameField = this.settings.getElementsByTagName('properties')[0].getElementsByTagName('projName')[0].textContent;
        this.projDescriptionField = this.settings.getElementsByTagName('properties')[0].getElementsByTagName('projDescription')[0].textContent;
        var xmlData = {projNameField: this.projNameField, projDescriptionField: this.projDescriptionField};
        console.log(xmlData);
    }

    /**
     * @method createItem
     * Creates a new row
     */
    get createItem() {
        return this._createItem;
    }

    set createItem(newValue) {
        this._createItem = newValue
    }

    // Can define constructor arguments if you wish.
    constructor() {
        console.log('Hello world');

        // If you define a constructor, always call super() first!
        // This is specific to CE and required by the spec.
        super();
        this._shadowRoot = this.attachShadow({
            'mode': 'open'
        });
        this._shadowRoot.appendChild(template.content.cloneNode(true));

        this.$projectRowsContainer = this._shadowRoot.querySelector('.project-rows-container');

        this.$projectCreate = this._shadowRoot.querySelector('.project-create');
        this.$projectCreate.addEventListener('click', e => {
            console.log("projectCreate", e);
            this.createItem();
        });

        this.$projectExport = this._shadowRoot.querySelector('.project-export');
        this.$projectExport.addEventListener('click', e => {
            // 'xlsx', csv'
            this.exportResults('csv');
        });

        // Setup a click listener on timeline-row-entry-text element.
        this.$projectRowsContainer.addEventListener('click', e => {
            console.log('click', e);
            let itemId = e.target.parentElement.getAttribute('id');
            console.log('itemId', itemId);
            this._openItem('', itemId);
        });
    }

    connectedCallback() {
        this.getGridResults().then((function (result) {
            console.log('result', result);
            this.generateView(result);
        }).bind(this));
    }

    disconnectedCallback() {
        // clean up subscriptions, events here
    }

    /**
     * @method generateView
     * Creates the UI using the list data passed in from custom-list-element
     */
    generateView(data) {
        System.import('moment').then(e => {
            if (data) {
                if (Array.isArray(data.columns)) {
                    if (Array.isArray(data.rows)) {

                        let htmlContent = '';

                        for (var iRow = 0; iRow < data.rows.length; iRow++) {
                            var projNameFieldValue = data.rows[iRow]['Properties'][this.projNameField];
                            var projDescriptionFieldValue = data.rows[iRow]['Properties'][this.projDescriptionField];
                            var itemId = data.rows[iRow].Identity.ItemId;
                            htmlContent += `<div class="project-row-entry" id='${itemId}'><div class="project-row-cell">${projNameFieldValue}</div><div class="project-row-cell">${projDescriptionFieldValue}</div></div>`;
                        }

                        this.$projectRowsContainer.innerHTML = htmlContent;
                    }
                }
            }
        });
    }
}

customElements.define('my-control', MyControl);