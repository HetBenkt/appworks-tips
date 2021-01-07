const template = document.createElement('template');
template.innerHTML = `
<style>
    :host {
    display: block;
    }
</style>
<div>
   <H1>Hello World</H1>
   <div>
      <button class="project-create" title="Create">Create</button>
   </div>
   <div style="width:100%" class="project-row-content-container">
		<div class="project-rows-container">
		</div>
   </div>
</div>
`;

class MyControl extends HTMLElement {
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
		var xmlData = { projNameField : this.projNameField, projDescriptionField : this.projDescriptionField };
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
        
		this.$myCreate = this._shadowRoot.querySelector('.project-create');
        this.$myCreate.addEventListener('click', e => {
            console.log("projectCreate", e);
            this.createItem();
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
							htmlContent += `<div class="project-row-entry"><div class="project-row-cell">${projNameFieldValue}</div><div class="project-row-cell">${projDescriptionFieldValue}</div></div>`;
						}
						
						this.$projectRowsContainer.innerHTML = htmlContent;
					}
				}
			}
		});
	}
}

customElements.define('my-control', MyControl);