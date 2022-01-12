/*
 * Unless explicitly acquired and licensed from Licensor under another license, the contents of
 * this file are subject to the Reciprocal Public License ("RPL") Version 1.5, or subsequent
 * versions as allowed by the RPL, and You may not copy or use this file in either source code
 * or executable form, except in compliance with the terms and conditions of the RPL
 *
 * All software distributed under the RPL is provided strictly on an "AS IS" basis, WITHOUT
 * WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND LICENSOR HEREBY DISCLAIMS ALL SUCH
 * WARRANTIES, INCLUDING WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, QUIET ENJOYMENT, OR NON-INFRINGEMENT. See the RPL for specific language
 * governing rights and limitations under the RPL.
 *
 * http://opensource.org/licenses/RPL-1.5
 *
 * Copyright 2012-2017 Open Justice Broker Consortium
 */
 
/* // List of input types
case "button": case "checkbox": case "color": case "date": case "datetime-local":
case "email": case "file": case "hidden": case "image": case "month": case "number":
case "password": case "radio": case "range": case "reset": case "search":
case "submit": case "tel": case "text": case "time": case "url": case "week": */
var INPUT_SHORTCUT_TYPES = [ "button", "text", "submit", "color", "url", "password",
	"hidden", "file", "number", "email", "reset", "date", "checkbox", "select", "radio" ];


function bootpopup(options) {
	// Create a new instance if this is not
	if(!(this instanceof bootpopup))
		return new bootpopup(options);

	var self = this;
	// Create a global random ID for the form
	this.formid = "bootpopup-form" + String(Math.random()).substr(2);

	this.options = {
		title: document.title,
		showclose: true,
		size: "normal",
		size_labels: "col-sm-4",
		size_inputs: "col-sm-8",
		content: [],
		onsubmit: "close",
		buttons: ["close"],

		before: function() {},
		dismiss: function() {},
		close: function() {},
		ok: function() {},
		cancel: function() {},
		yes: function() {},
		no: function() {},
		complete: function() {},
		submit: function(e) {
			self.callback(self.options.onsubmit, e);
			return false;	// Cancel form submision
		}
	};

	this.addOptions = function(options) {
		var buttons = [];
		for(var key in options) {
			if(key in this.options)
				this.options[key] = options[key];
			// If an event for a button is given, show the respective button
			if(["close", "ok", "cancel", "yes", "no"].indexOf(key) >= 0)
				buttons.push(key);
		}
		// Copy news buttons to this.options.buttons
		if(buttons.length > 0) {
			// Clear default buttons if new are not given
			if(!("buttons" in options)) this.options.buttons = [];
			buttons.forEach(function(item) {
				if(self.options.buttons.indexOf(item) < 0)
					self.options.buttons.push(item);
			});
		}
		// Determine what is the best action if none is given
		if(typeof options.onsubmit !== "string") {
			if(this.options.buttons.indexOf("close") > 0) this.options.onsubmit = "close";
			else if(this.options.buttons.indexOf("ok") > 0) this.options.onsubmit = "ok";
			else if(this.options.buttons.indexOf("yes") > 0) this.options.onsubmit = "yes";
		}

		return this.options;
	};

	this.setOptions = function(options) {
		this.options = options;
		return this.options;
	};

	this.create = function() {
		// Option for modal dialog size
		var classModalDialog = "modal-dialog modal-dialog-centered";
		if(this.options.size == "large") classModalDialog += " modal-lg";
		if(this.options.size == "small") classModalDialog += " modal-sm";

		// Create HTML elements for modal dialog
		this.modal = $('<div class="modal fade" tabindex="-1" role="dialog" aria-labelledby="bootpopup-title"></div>');
		this.dialog = $('<div></div>', { class: classModalDialog, role: "document" });
		this.content = $('<div class="modal-content"></div>');
		this.dialog.append(this.content);
		this.modal.append(this.dialog);

		// Header
		this.header = $('<div class="modal-header"></div>');
		this.header.append('<h5 class="modal-title" id="bootpopup-title">' + this.options.title + '</h4>');
		if(this.options.showclose)	// Close button
			this.header.append('<button type="button" class="bootpopup-button close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>');
		this.content.append(this.header);

		// Body
		this.body = $('<div class="modal-body"></div>');
		this.form = $("<form></form>", { id: this.formid, class: "form-horizontal", submit: function(e) { return self.options.submit(e); } });
		this.body.append(this.form);
		this.content.append(this.body);

		// Iterate over entries
		for(var i in this.options.content) {
			var entry = this.options.content[i];
			switch(typeof entry) {
			case "string":		// HTML string
				this.form.append(entry);
				break;
			case "object":
				for(var type in entry) {
					var attrs = entry[type];

					// Convert functions to string to be used as callback
					for(var attribute in attrs)
						if(typeof attrs[attribute] === "function")
							attrs[attribute] = "(" + attrs[attribute] + ")(this)";

					// Check if type is a shortcut for input
					if(INPUT_SHORTCUT_TYPES.indexOf(type) >= 0) {
						attrs.type = type;  // Add attribute for type
						type = "input";     // Continue to input
					}

					if(type == "input") {
						// To avoid adding "form-control" class
						if(attrs.type === "checkbox" && typeof attrs.class === "undefined")
							attrs.class = "";

						// Create a random id for the input if none provided
						attrs.id = (typeof attrs.id === "undefined" ? "bootpopup-input" + String(Math.random()).substr(2) : attrs.id);
						attrs.class = (typeof attrs.class === "undefined" ? "form-control" : attrs.class);
						attrs.type = (typeof attrs.type === "undefined" ? "text" : attrs.type);


						// Create input
						var input;
						switch(attrs.type) {
							case "checkbox":
								// Special case for checkbox
								input = $('<div class="checkbox"></div>')
									.append($('<label></label>')
										.append($("<input />", attrs))
										.append(attrs.label));
								
								// Clear label to not be added as header
								attrs.label = "";
								break;
							case "select":
								// Special case for select
								input = $("<select></select>", attrs);
								for(var option in attrs.options)
									input.append($("<option></option>", { value: option })
										.append(attrs.options[option]));
								
								break;
							case "radio":
								// Special case for radios
								input = [];
								for(var option in attrs.options)
									input.push($('<div class="radio"></div>', attrs)
										.append($('<label></label>')
											.append($("<input />", { type: "radio", name: attrs.name, value: option }))
											.append(attrs.options[option])));
								break;
							default:
								input = $("<input />", attrs);
						}

						// Form Group
						var formGroup = $('<div class="form-group"></div>').appendTo(this.form);
						// Label
						$("<label></label>", { for: attrs.id, class: "control-label " + this.options.size_labels, text: attrs.label }).appendTo(formGroup);

						// Input and div to control width
						var divInput = $('<div></div>', { class: this.options.size_inputs });
						divInput.append(input);
						formGroup.append(divInput);
					}
					else	// Anything else besides input
						this.form.append($("<" + type + "></" + type + ">", attrs));	// Add directly
				}
				break;
			default:
				throw "Invalid entry type";
			}
		}

		// Footer
		this.footer = $('<div class="modal-footer"></div>');
		this.content.append(this.footer);

		for(var key in this.options.buttons) {
			var item = this.options.buttons[key];
			var btnClass = "";
			var btnText = "";

			switch(item) {
			case "close": btnClass = "btn-primary"; btnText = "Close"; break;
			case "ok": btnClass = "btn-primary"; btnText = "OK"; break;
			case "cancel": btnClass = "btn-default"; btnText = "Cancel"; break;
			case "yes": btnClass = "btn-primary"; btnText = "Yes"; break;
			case "no": btnClass = "btn-default"; btnText = "No"; break;
			}

			var button = $("<button></button>", {
				type: "button",
				text: btnText,
				class: "btn " + btnClass,
				"data-dismiss": "modal",
				"data-callback": item,
				"data-form": this.formid,

				click: function(event) {
					var name = $(event.target).data("callback");
					self.callback(name, event);
				}
			});
			this.footer.append(button);

			// Reference for buttons
			switch(item) {
			case "close": this.btnClose = button; break;
			case "ok": this.btnOk = button; break;
			case "cancel": this.btnCancel = button; break;
			case "yes": this.btnYes = button; break;
			case "no": this.btnNo = button; break;
			}
		}

		// Setup events for dismiss and complete
		this.modal.on('hide.bs.modal', this.options.dismiss);
		this.modal.on('hidden.bs.modal', function(e) {
			self.options.complete(e);
			self.modal.remove();	// Delete window after complete
		});

		// Don't close on backdrop click
		if(this.options.showclose === false)
			this.modal.attr('data-backdrop', 'static');

		// Add window to body
		$(document.body).append(this.modal);
	};

	this.show = function() {
		// Call before event
		this.options.before(this);

		// Fire the modal window
		this.modal.modal();
	};

	this.data = function() {
		var keyval = {};
		var array = this.form.serializeArray();
		for(var i in array) {
			var name = array[i].name, val = array[i].value;

			if(typeof keyval[name] === "undefined")
				keyval[name] = val;
			else {
				if(!Array.isArray(keyval[name]))
					keyval[name] = [keyval[name]];
				keyval[name].push(val);
			}

		}
		return keyval;
	};

	this.callback = function(name, event) {
		var func = this.options[name];		// Get function to call
		if(typeof func !== "function") return;

		// Perform callback
		var array = this.form.serializeArray();
		var ret = func(this.data(), array, event);

		// Hide window
		this.modal.modal("hide");
		return ret;
	};

	this.dismiss = function() { this.callback("dismiss"); };
	this.submit = function() { this.callback("submit"); };
	this.close = function() { this.callback("close"); };
	this.ok = function() { this.callback("ok"); };
	this.cancel = function() { this.callback("cancel"); };
	this.yes = function() { this.callback("yes"); };
	this.no = function() { this.callback("no"); };

	this.addOptions(options);
	this.create();
	this.show();
}


bootpopup.alert = function(message, title, callback) {
	if(typeof title === "function")
		callback = title;

	if(typeof title !== "string")
		title = document.title;
	if(typeof callback !== "function")
		callback = function() {};

	return bootpopup({
		title: title,
		content: [{ p: { text: message } }],
		dismiss: function() { callback(); }
	});
};

bootpopup.confirm = function(message, title, callback) {
	if(typeof title === "function")
		callback = title;

	if(typeof title !== "string")
		title = document.title;
	if(typeof callback !== "function")
		callback = function() {};

	var answer = false;
	return bootpopup({
		title: title,
		showclose: false,
		content: [{ p: { text: message } }],
		buttons: ["no", "yes"],
		yes: function() { answer = true; },
		dismiss: function() { callback(answer); }
	});
};

bootpopup.prompt = function(label, type, message, title, callback) {
	// Callback can be in any position, except label
	var callback_function = function() {};
	if(typeof type === "function")
		callback_function = type;
	if(typeof message === "function")
		callback_function = message;
	if(typeof title === "function")
		callback_function = title;
	if(typeof callback === "function")
		callback_function = callback;

	// If a list of values is provided, then the parameters are shifted
	// because type should be ignored
	if(typeof label === "object") {
		title = message;
		message = type;
		type = null;
	}

	// Sanitize message and title
	if(typeof message !== "string")
		message = "Please, provide values for:";
	if(typeof title !== "string")
		title = document.title;

	// Add message to the window
	var content = [{ p: { text: message } }];

	// If label is a list of values to be asked to input
	if(typeof label === "object") {
		label.forEach(function(entry) {
			var obj = null;

			// HTML
			if (typeof entry === "string")
				obj = entry;
			
			// Input
			if ( entry !== null && typeof entry === "object" && typeof entry.label === "string") {
				if(typeof entry.name !== "string")  // Name in lower case and dashes instead spaces
					entry.name = entry.label.toLowerCase().replace(/\s+/g, "-");
				if(typeof entry.type !== "string")
					entry.type = "text";
				
				obj = { input: entry };
			}

			if(obj !== null)
				content.push(obj);
		});
	}
	else {
		if(typeof type !== "string") type = "text";
		content.push({ input: { type: type, name: "value", label: label } });
		var callback_tmp = callback_function;   // Overload callback function to return "data.value"
		callback_function = function(data) { callback_tmp(data.value); };
	}

	return bootpopup({
		title: title,
		content: content,
		buttons: ["cancel", "ok"],
		ok: function(data) {
			callback_function(data);
		}
	});
};

/**
 * AMD support: require.js
 */
if(typeof define === "function") {
	define(["jquery", "bootstrap"], function() {
		return bootpopup;
	});
}
