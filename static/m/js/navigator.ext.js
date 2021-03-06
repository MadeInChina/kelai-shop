/**	navigator [extended]
 *	Simply extends Browsers navigator Object to include browser name, version number, and mobile type (if available).
 *
 *	@property {String} browser The name of the browser.
 *	@property {Double} version The current Browser version number.
 *	@property {String|Boolean} mobile Will be `false` if is not found to be mobile device. Else, will be best guess Name of Mobile Device (not to be confused with browser name)
 *	@property {Boolean} webkit If is webkit or not.
 *
 */
;(function() {
	function init() {
		try {
			if (navigator && navigator['userAgent']) {
				navigator.browser = setBrowser();
				navigator.mobile = setMobile();
				navigator.version = setVersion();
				navigator.webkit = setWebkit();
				return true;
			}
		} catch(err) {}
		throw new Error("Browser does not support `navigator` Object |OR| has undefined `userAgent` property.");
	}
	
	function setBrowser() {
		try {
			switch (true) {
				case (/MSIE|Trident/i.test(navigator.userAgent)): return 'MSIE';
				case (/Chrome/.test(navigator.userAgent)): return 'Chrome';
				case (/Opera/.test(navigator.userAgent)): return 'Opera';
				case (/Kindle|Silk|KFTT|KFOT|KFJWA|KFJWI|KFSOWI|KFTHWA|KFTHWI|KFAPWA|KFAPWI/i.test(navigator.userAgent)):
					return (/Silk/i.test(navigator.userAgent)) ? 'Silk' : 'Kindle';
				case (/BlackBerry/.test(navigator.userAgent)): return 'BlackBerry';
				case (/PlayBook/.test(navigator.userAgent)): return 'PlayBook';
				case (/BB[0-9]{1,}; Touch/.test(navigator.userAgent)): return 'Blackberry';
				case (/Android/.test(navigator.userAgent)): return 'Android';
				case (/Safari/.test(navigator.userAgent)): return 'Safari';
				case (/Firefox/.test(navigator.userAgent)): return 'Mozilla';
				case (/Nokia/.test(navigator.userAgent)): return 'Nokia';
			}
		}
		catch(err) { console.debug("ERROR:setBrowser\t", err); }
		return void 0;
	}
	
	function setMobile() {
		try {
			switch (true) {
				case (/Sony[^ ]*/i.test(navigator.userAgent)): return 'Sony';
				case (/RIM Tablet/i.test(navigator.userAgent)): return 'RIM Tablet';
				case (/BlackBerry/i.test(navigator.userAgent)): return 'BlackBerry';
				case (/iPhone/i.test(navigator.userAgent)): return 'iPhone';
				case (/iPad/i.test(navigator.userAgent)): return 'iPad';
				case (/iPod/i.test(navigator.userAgent)): return 'iPod';
				case (/Opera Mini/i.test(navigator.userAgent)): return 'Opera Mini';
				case (/IEMobile/i.test(navigator.userAgent)): return 'IEMobile';
				case (/BB[0-9]{1,}; Touch/i.test(navigator.userAgent)): return 'BlackBerry';
				case (/Nokia/i.test(navigator.userAgent)): return 'Nokia';
				case (/Android/i.test(navigator.userAgent)): return 'Android';
			}
		}
		catch(err) { console.debug("ERROR:setMobile\t", err); }
		return !1;
	}
	
	function setVersion() {
		try {	//	TODO: consider using end cap in regex such as RegExp(/ Firefox\/\d+(\.\d+){0,1}$/)
			switch (true) {
				case (/MSIE|Trident/i.test(navigator.userAgent)):
					if (/Trident/i.test(navigator.userAgent) && /rv:([0-9]{1,}[\.0-9]{0,})/.test(navigator.userAgent)) return parseFloat(navigator.userAgent.match(/rv:([0-9]{1,}[\.0-9]{0,})/)[1].replace(/[^0-9\.]/g, ''));
					return (/MSIE/i.test(navigator.userAgent) && parseFloat(navigator.userAgent.split("MSIE")[1].replace(/[^0-9\.]/g, '')) > 0) ? parseFloat(navigator.userAgent.split("MSIE")[1].replace(/[^0-9\.]/g, '')) : "Edge";
				case (/Chrome/.test(navigator.userAgent)):
					return parseFloat(navigator.userAgent.split("Chrome/")[1].split("Safari")[0].replace(/[^0-9\.]/g, ''));
				case (/Opera/.test(navigator.userAgent)):
					return parseFloat(navigator.userAgent.split("Version/")[1].replace(/[^0-9\.]/g, ''));
				case (/Kindle|Silk|KFTT|KFOT|KFJWA|KFJWI|KFSOWI|KFTHWA|KFTHWI|KFAPWA|KFAPWI/i.test(navigator.userAgent)):
					if (/Silk/i.test(navigator.userAgent)) return parseFloat(navigator.userAgent.split("Silk/")[1].split("Safari")[0].replace(/[^0-9\.]/g, ''));
					else if (/Kindle/i.test(navigator.userAgent) && /Version/i.test(navigator.userAgent)) return parseFloat(navigator.userAgent.split("Version/")[1].split("Safari")[0].replace(/[^0-9\.]/g, ''));
				case (/BlackBerry/.test(navigator.userAgent)):
					return parseFloat(navigator.userAgent.split("/")[1].replace(/[^0-9\.]/g, ''));
				case (/PlayBook/.test(navigator.userAgent)):
				case (/BB[0-9]{1,}; Touch/.test(navigator.userAgent)):
				case (/Safari/.test(navigator.userAgent)):
					return parseFloat(navigator.userAgent.split("Version/")[1].split("Safari")[0].replace(/[^0-9\.]/g, ''));
				case (/Firefox/.test(navigator.userAgent)):
					return parseFloat(navigator.userAgent.split(/Firefox\//i)[1].replace(/[^0-9\.]/g, ''));
				case (/Android/.test(navigator.userAgent)):
					return parseFloat(navigator.userAgent.split("Version/")[1].split("Safari")[0].replace(/[^0-9\.]/g, ''));
				case (/Nokia/.test(navigator.userAgent)):
					return parseFloat(navigator.userAgent.split('Browser')[1].replace(/[^0-9\.]/g, ''));
			}
		}
		catch(err) { console.debug("ERROR:setVersion\t", err); }
		return void 0;
	}
	
	function setWebkit() {
		try { return /WebKit/i.test(navigator.userAgent); } catch(err) { console.debug("ERROR:setWebkit\t", err); }
		return void 0;
	}
	
	init();
})();
