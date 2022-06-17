(function webpackUniversalModuleDefinition(root, factory) {
    if(typeof exports === 'object' && typeof module === 'object')
        module.exports = factory(require("vue"));
    else if(typeof define === 'function' && define.amd)
        define([], factory);
    else if(typeof exports === 'object')
        exports["contextmenu"] = factory(require("vue"));
    else
        root["contextmenu"] = factory(root["Vue"]);
})((typeof self !== 'undefined' ? self : this), function(__WEBPACK_EXTERNAL_MODULE__8bbf__) {
    return /******/ (function(modules) { // webpackBootstrap
        /******/ 	// The module cache
        /******/ 	var installedModules = {};
        /******/
        /******/ 	// The require function
        /******/ 	function __webpack_require__(moduleId) {
            /******/
            /******/ 		// Check if module is in cache
            /******/ 		if(installedModules[moduleId]) {
                /******/ 			return installedModules[moduleId].exports;
                /******/ 		}
            /******/ 		// Create a new module (and put it into the cache)
            /******/ 		var module = installedModules[moduleId] = {
                /******/ 			i: moduleId,
                /******/ 			l: false,
                /******/ 			exports: {}
                /******/ 		};
            /******/
            /******/ 		// Execute the module function
            /******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);
            /******/
            /******/ 		// Flag the module as loaded
            /******/ 		module.l = true;
            /******/
            /******/ 		// Return the exports of the module
            /******/ 		return module.exports;
            /******/ 	}
        /******/
        /******/
        /******/ 	// expose the modules object (__webpack_modules__)
        /******/ 	__webpack_require__.m = modules;
        /******/
        /******/ 	// expose the module cache
        /******/ 	__webpack_require__.c = installedModules;
        /******/
        /******/ 	// define getter function for harmony exports
        /******/ 	__webpack_require__.d = function(exports, name, getter) {
            /******/ 		if(!__webpack_require__.o(exports, name)) {
                /******/ 			Object.defineProperty(exports, name, { enumerable: true, get: getter });
                /******/ 		}
            /******/ 	};
        /******/
        /******/ 	// define __esModule on exports
        /******/ 	__webpack_require__.r = function(exports) {
            /******/ 		if(typeof Symbol !== 'undefined' && Symbol.toStringTag) {
                /******/ 			Object.defineProperty(exports, Symbol.toStringTag, { value: 'Module' });
                /******/ 		}
            /******/ 		Object.defineProperty(exports, '__esModule', { value: true });
            /******/ 	};
        /******/
        /******/ 	// create a fake namespace object
        /******/ 	// mode & 1: value is a module id, require it
        /******/ 	// mode & 2: merge all properties of value into the ns
        /******/ 	// mode & 4: return value when already ns object
        /******/ 	// mode & 8|1: behave like require
        /******/ 	__webpack_require__.t = function(value, mode) {
            /******/ 		if(mode & 1) value = __webpack_require__(value);
            /******/ 		if(mode & 8) return value;
            /******/ 		if((mode & 4) && typeof value === 'object' && value && value.__esModule) return value;
            /******/ 		var ns = Object.create(null);
            /******/ 		__webpack_require__.r(ns);
            /******/ 		Object.defineProperty(ns, 'default', { enumerable: true, value: value });
            /******/ 		if(mode & 2 && typeof value != 'string') for(var key in value) __webpack_require__.d(ns, key, function(key) { return value[key]; }.bind(null, key));
            /******/ 		return ns;
            /******/ 	};
        /******/
        /******/ 	// getDefaultExport function for compatibility with non-harmony modules
        /******/ 	__webpack_require__.n = function(module) {
            /******/ 		var getter = module && module.__esModule ?
                /******/ 			function getDefault() { return module['default']; } :
                /******/ 			function getModuleExports() { return module; };
            /******/ 		__webpack_require__.d(getter, 'a', getter);
            /******/ 		return getter;
            /******/ 	};
        /******/
        /******/ 	// Object.prototype.hasOwnProperty.call
        /******/ 	__webpack_require__.o = function(object, property) { return Object.prototype.hasOwnProperty.call(object, property); };
        /******/
        /******/ 	// __webpack_public_path__
        /******/ 	__webpack_require__.p = "";
        /******/
        /******/
        /******/ 	// Load entry module and return exports
        /******/ 	return __webpack_require__(__webpack_require__.s = "8886");
        /******/ })
    /************************************************************************/
    /******/ ({

        /***/ "19dd":
        /***/ (function(module, exports, __webpack_require__) {

// Imports
            var ___CSS_LOADER_API_IMPORT___ = __webpack_require__("4210");
            exports = ___CSS_LOADER_API_IMPORT___(false);
// Module
            exports.push([module.i, ".contextmenu-submenu-fade-enter-active,.contextmenu-submenu-fade-leave-active{-webkit-transition:opacity .1s;transition:opacity .1s}.contextmenu-submenu-fade-enter,.contextmenu-submenu-fade-leave-to{opacity:0}", ""]);
// Exports
            module.exports = exports;


            /***/ }),

        /***/ "2b35":
        /***/ (function(module, exports, __webpack_require__) {

// Imports
            var ___CSS_LOADER_API_IMPORT___ = __webpack_require__("4210");
            exports = ___CSS_LOADER_API_IMPORT___(false);
// Module
            exports.push([module.i, ".Contextmenu_menu_cYpEk,.Contextmenu_menu_item_1DQNs,.Contextmenu_menu_item__clickable_29d8g,.Contextmenu_menu_item__unclickable_2nbwq{box-sizing:border-box}", ""]);
// Exports
            exports.locals = {
                "menu": "Contextmenu_menu_cYpEk",
                "menu_item": "Contextmenu_menu_item_1DQNs",
                "menu_item__clickable": "Contextmenu_menu_item__clickable_29d8g",
                "menu_item__unclickable": "Contextmenu_menu_item__unclickable_2nbwq"
            };
            module.exports = exports;


            /***/ }),

        /***/ "2d58":
        /***/ (function(module, exports, __webpack_require__) {

// style-loader: Adds some css to the DOM by adding a <style> tag

// load the styles
            var content = __webpack_require__("903b");
            if(typeof content === 'string') content = [[module.i, content, '']];
            if(content.locals) module.exports = content.locals;
// add the styles to the DOM
            var add = __webpack_require__("3cc6").default
            var update = add("7bb56876", content, true, {"sourceMap":false,"shadowMode":false});

            /***/ }),

        /***/ "3cc6":
        /***/ (function(module, __webpack_exports__, __webpack_require__) {

            "use strict";
            __webpack_require__.r(__webpack_exports__);

// CONCATENATED MODULE: C:/Users/Laziji/AppData/Roaming/npm/node_modules/@vue/cli-service/node_modules/vue-style-loader/lib/listToStyles.js
            /**
             * Translates the list format produced by css-loader into something
             * easier to manipulate.
             */
            function listToStyles (parentId, list) {
                var styles = []
                var newStyles = {}
                for (var i = 0; i < list.length; i++) {
                    var item = list[i]
                    var id = item[0]
                    var css = item[1]
                    var media = item[2]
                    var sourceMap = item[3]
                    var part = {
                        id: parentId + ':' + i,
                        css: css,
                        media: media,
                        sourceMap: sourceMap
                    }
                    if (!newStyles[id]) {
                        styles.push(newStyles[id] = { id: id, parts: [part] })
                    } else {
                        newStyles[id].parts.push(part)
                    }
                }
                return styles
            }

// CONCATENATED MODULE: C:/Users/Laziji/AppData/Roaming/npm/node_modules/@vue/cli-service/node_modules/vue-style-loader/lib/addStylesClient.js
            /* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "default", function() { return addStylesClient; });
            /*
              MIT License http://www.opensource.org/licenses/mit-license.php
              Author Tobias Koppers @sokra
              Modified by Evan You @yyx990803
            */



            var hasDocument = typeof document !== 'undefined'

            if (typeof DEBUG !== 'undefined' && DEBUG) {
                if (!hasDocument) {
                    throw new Error(
                        'vue-style-loader cannot be used in a non-browser environment. ' +
                        "Use { target: 'node' } in your Webpack config to indicate a server-rendering environment."
                    ) }
            }

            /*
            type StyleObject = {
              id: number;
              parts: Array<StyleObjectPart>
            }

            type StyleObjectPart = {
              css: string;
              media: string;
              sourceMap: ?string
            }
            */

            var stylesInDom = {/*
  [id: number]: {
    id: number,
    refs: number,
    parts: Array<(obj?: StyleObjectPart) => void>
  }
*/}

            var head = hasDocument && (document.head || document.getElementsByTagName('head')[0])
            var singletonElement = null
            var singletonCounter = 0
            var isProduction = false
            var noop = function () {}
            var options = null
            var ssrIdKey = 'data-vue-ssr-id'

// Force single-tag solution on IE6-9, which has a hard limit on the # of <style>
// tags it will allow on a page
            var isOldIE = typeof navigator !== 'undefined' && /msie [6-9]\b/.test(navigator.userAgent.toLowerCase())

            function addStylesClient (parentId, list, _isProduction, _options) {
                isProduction = _isProduction

                options = _options || {}

                var styles = listToStyles(parentId, list)
                addStylesToDom(styles)

                return function update (newList) {
                    var mayRemove = []
                    for (var i = 0; i < styles.length; i++) {
                        var item = styles[i]
                        var domStyle = stylesInDom[item.id]
                        domStyle.refs--
                        mayRemove.push(domStyle)
                    }
                    if (newList) {
                        styles = listToStyles(parentId, newList)
                        addStylesToDom(styles)
                    } else {
                        styles = []
                    }
                    for (var i = 0; i < mayRemove.length; i++) {
                        var domStyle = mayRemove[i]
                        if (domStyle.refs === 0) {
                            for (var j = 0; j < domStyle.parts.length; j++) {
                                domStyle.parts[j]()
                            }
                            delete stylesInDom[domStyle.id]
                        }
                    }
                }
            }

            function addStylesToDom (styles /* Array<StyleObject> */) {
                for (var i = 0; i < styles.length; i++) {
                    var item = styles[i]
                    var domStyle = stylesInDom[item.id]
                    if (domStyle) {
                        domStyle.refs++
                        for (var j = 0; j < domStyle.parts.length; j++) {
                            domStyle.parts[j](item.parts[j])
                        }
                        for (; j < item.parts.length; j++) {
                            domStyle.parts.push(addStyle(item.parts[j]))
                        }
                        if (domStyle.parts.length > item.parts.length) {
                            domStyle.parts.length = item.parts.length
                        }
                    } else {
                        var parts = []
                        for (var j = 0; j < item.parts.length; j++) {
                            parts.push(addStyle(item.parts[j]))
                        }
                        stylesInDom[item.id] = { id: item.id, refs: 1, parts: parts }
                    }
                }
            }

            function createStyleElement () {
                var styleElement = document.createElement('style')
                styleElement.type = 'text/css'
                head.appendChild(styleElement)
                return styleElement
            }

            function addStyle (obj /* StyleObjectPart */) {
                var update, remove
                var styleElement = document.querySelector('style[' + ssrIdKey + '~="' + obj.id + '"]')

                if (styleElement) {
                    if (isProduction) {
                        // has SSR styles and in production mode.
                        // simply do nothing.
                        return noop
                    } else {
                        // has SSR styles but in dev mode.
                        // for some reason Chrome can't handle source map in server-rendered
                        // style tags - source maps in <style> only works if the style tag is
                        // created and inserted dynamically. So we remove the server rendered
                        // styles and inject new ones.
                        styleElement.parentNode.removeChild(styleElement)
                    }
                }

                if (isOldIE) {
                    // use singleton mode for IE9.
                    var styleIndex = singletonCounter++
                    styleElement = singletonElement || (singletonElement = createStyleElement())
                    update = applyToSingletonTag.bind(null, styleElement, styleIndex, false)
                    remove = applyToSingletonTag.bind(null, styleElement, styleIndex, true)
                } else {
                    // use multi-style-tag mode in all other cases
                    styleElement = createStyleElement()
                    update = applyToTag.bind(null, styleElement)
                    remove = function () {
                        styleElement.parentNode.removeChild(styleElement)
                    }
                }

                update(obj)

                return function updateStyle (newObj /* StyleObjectPart */) {
                    if (newObj) {
                        if (newObj.css === obj.css &&
                            newObj.media === obj.media &&
                            newObj.sourceMap === obj.sourceMap) {
                            return
                        }
                        update(obj = newObj)
                    } else {
                        remove()
                    }
                }
            }

            var replaceText = (function () {
                var textStore = []

                return function (index, replacement) {
                    textStore[index] = replacement
                    return textStore.filter(Boolean).join('\n')
                }
            })()

            function applyToSingletonTag (styleElement, index, remove, obj) {
                var css = remove ? '' : obj.css

                if (styleElement.styleSheet) {
                    styleElement.styleSheet.cssText = replaceText(index, css)
                } else {
                    var cssNode = document.createTextNode(css)
                    var childNodes = styleElement.childNodes
                    if (childNodes[index]) styleElement.removeChild(childNodes[index])
                    if (childNodes.length) {
                        styleElement.insertBefore(cssNode, childNodes[index])
                    } else {
                        styleElement.appendChild(cssNode)
                    }
                }
            }

            function applyToTag (styleElement, obj) {
                var css = obj.css
                var media = obj.media
                var sourceMap = obj.sourceMap

                if (media) {
                    styleElement.setAttribute('media', media)
                }
                if (options.ssrId) {
                    styleElement.setAttribute(ssrIdKey, obj.id)
                }

                if (sourceMap) {
                    // https://developer.chrome.com/devtools/docs/javascript-debugging
                    // this makes source maps inside style tags work properly in Chrome
                    css += '\n/*# sourceURL=' + sourceMap.sources[0] + ' */'
                    // http://stackoverflow.com/a/26603875
                    css += '\n/*# sourceMappingURL=data:application/json;base64,' + btoa(unescape(encodeURIComponent(JSON.stringify(sourceMap)))) + ' */'
                }

                if (styleElement.styleSheet) {
                    styleElement.styleSheet.cssText = css
                } else {
                    while (styleElement.firstChild) {
                        styleElement.removeChild(styleElement.firstChild)
                    }
                    styleElement.appendChild(document.createTextNode(css))
                }
            }


            /***/ }),

        /***/ "4210":
        /***/ (function(module, exports, __webpack_require__) {

            "use strict";


            /*
              MIT License http://www.opensource.org/licenses/mit-license.php
              Author Tobias Koppers @sokra
            */
// css base code, injected by the css-loader
// eslint-disable-next-line func-names
            module.exports = function (useSourceMap) {
                var list = []; // return the list of modules as css string

                list.toString = function toString() {
                    return this.map(function (item) {
                        var content = cssWithMappingToString(item, useSourceMap);

                        if (item[2]) {
                            return "@media ".concat(item[2], " {").concat(content, "}");
                        }

                        return content;
                    }).join('');
                }; // import a list of modules into the list
                // eslint-disable-next-line func-names


                list.i = function (modules, mediaQuery) {
                    if (typeof modules === 'string') {
                        // eslint-disable-next-line no-param-reassign
                        modules = [[null, modules, '']];
                    }

                    for (var i = 0; i < modules.length; i++) {
                        var item = [].concat(modules[i]);

                        if (mediaQuery) {
                            if (!item[2]) {
                                item[2] = mediaQuery;
                            } else {
                                item[2] = "".concat(mediaQuery, " and ").concat(item[2]);
                            }
                        }

                        list.push(item);
                    }
                };

                return list;
            };

            function cssWithMappingToString(item, useSourceMap) {
                var content = item[1] || ''; // eslint-disable-next-line prefer-destructuring

                var cssMapping = item[3];

                if (!cssMapping) {
                    return content;
                }

                if (useSourceMap && typeof btoa === 'function') {
                    var sourceMapping = toComment(cssMapping);
                    var sourceURLs = cssMapping.sources.map(function (source) {
                        return "/*# sourceURL=".concat(cssMapping.sourceRoot).concat(source, " */");
                    });
                    return [content].concat(sourceURLs).concat([sourceMapping]).join('\n');
                }

                return [content].join('\n');
            } // Adapted from convert-source-map (MIT)


            function toComment(sourceMap) {
                // eslint-disable-next-line no-undef
                var base64 = btoa(unescape(encodeURIComponent(JSON.stringify(sourceMap))));
                var data = "sourceMappingURL=data:application/json;charset=utf-8;base64,".concat(base64);
                return "/*# ".concat(data, " */");
            }

            /***/ }),

        /***/ "562f":
        /***/ (function(module, exports, __webpack_require__) {

// style-loader: Adds some css to the DOM by adding a <style> tag

// load the styles
            var content = __webpack_require__("2b35");
            if(typeof content === 'string') content = [[module.i, content, '']];
            if(content.locals) module.exports = content.locals;
// add the styles to the DOM
            var add = __webpack_require__("3cc6").default
            var update = add("21a64ddc", content, true, {"sourceMap":false,"shadowMode":false});

            /***/ }),

        /***/ "6187":
        /***/ (function(module, __webpack_exports__, __webpack_require__) {

            "use strict";
            /* harmony import */ var _C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_vue_style_loader_index_js_ref_6_oneOf_0_0_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_css_loader_dist_cjs_js_ref_6_oneOf_0_1_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_vue_loader_lib_loaders_stylePostLoader_js_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_postcss_loader_src_index_js_ref_6_oneOf_0_2_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_postcss_loader_src_index_js_ref_6_oneOf_0_3_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_cache_loader_dist_cjs_js_ref_0_0_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_vue_loader_lib_index_js_vue_loader_options_Contextmenu_vue_vue_type_style_index_0_module_true_lang_css___WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__("562f");
            /* harmony import */ var _C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_vue_style_loader_index_js_ref_6_oneOf_0_0_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_css_loader_dist_cjs_js_ref_6_oneOf_0_1_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_vue_loader_lib_loaders_stylePostLoader_js_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_postcss_loader_src_index_js_ref_6_oneOf_0_2_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_postcss_loader_src_index_js_ref_6_oneOf_0_3_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_cache_loader_dist_cjs_js_ref_0_0_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_vue_loader_lib_index_js_vue_loader_options_Contextmenu_vue_vue_type_style_index_0_module_true_lang_css___WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_vue_style_loader_index_js_ref_6_oneOf_0_0_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_css_loader_dist_cjs_js_ref_6_oneOf_0_1_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_vue_loader_lib_loaders_stylePostLoader_js_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_postcss_loader_src_index_js_ref_6_oneOf_0_2_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_postcss_loader_src_index_js_ref_6_oneOf_0_3_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_cache_loader_dist_cjs_js_ref_0_0_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_vue_loader_lib_index_js_vue_loader_options_Contextmenu_vue_vue_type_style_index_0_module_true_lang_css___WEBPACK_IMPORTED_MODULE_0__);
            /* harmony default export */ __webpack_exports__["default"] = (_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_vue_style_loader_index_js_ref_6_oneOf_0_0_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_css_loader_dist_cjs_js_ref_6_oneOf_0_1_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_vue_loader_lib_loaders_stylePostLoader_js_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_postcss_loader_src_index_js_ref_6_oneOf_0_2_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_postcss_loader_src_index_js_ref_6_oneOf_0_3_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_cache_loader_dist_cjs_js_ref_0_0_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_vue_loader_lib_index_js_vue_loader_options_Contextmenu_vue_vue_type_style_index_0_module_true_lang_css___WEBPACK_IMPORTED_MODULE_0___default.a);

            /***/ }),

        /***/ "8886":
        /***/ (function(module, __webpack_exports__, __webpack_require__) {

            "use strict";
            __webpack_require__.r(__webpack_exports__);

// CONCATENATED MODULE: C:/Users/Laziji/AppData/Roaming/npm/node_modules/@vue/cli-service/lib/commands/build/setPublicPath.js
// This file is imported into lib/wc client bundles.

            if (typeof window !== 'undefined') {
                if (true) {
                    __webpack_require__("e31d")
                }

                var i
                if ((i = window.document.currentScript) && (i = i.src.match(/(.+\/)[^/]+\.js(\?.*)?$/))) {
                    __webpack_require__.p = i[1] // eslint-disable-line
                }
            }

// Indicate to webpack that this file can be concatenated
            /* harmony default export */ var setPublicPath = (null);

// EXTERNAL MODULE: external {"commonjs":"vue","commonjs2":"vue","root":"Vue"}
            var external_commonjs_vue_commonjs2_vue_root_Vue_ = __webpack_require__("8bbf");
            var external_commonjs_vue_commonjs2_vue_root_Vue_default = /*#__PURE__*/__webpack_require__.n(external_commonjs_vue_commonjs2_vue_root_Vue_);

// CONCATENATED MODULE: C:/Users/Laziji/AppData/Roaming/npm/node_modules/@vue/cli-service/node_modules/cache-loader/dist/cjs.js?{"cacheDirectory":"node_modules/.cache/vue-loader","cacheIdentifier":"9fd667e4-vue-loader-template"}!C:/Users/Laziji/AppData/Roaming/npm/node_modules/@vue/cli-service/node_modules/vue-loader/lib/loaders/templateLoader.js??vue-loader-options!C:/Users/Laziji/AppData/Roaming/npm/node_modules/@vue/cli-service/node_modules/cache-loader/dist/cjs.js??ref--0-0!C:/Users/Laziji/AppData/Roaming/npm/node_modules/@vue/cli-service/node_modules/vue-loader/lib??vue-loader-options!./src/components/Contextmenu.vue?vue&type=template&id=9e47a7a8&
            var render = function () {var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;return _c('div')}
            var staticRenderFns = []


// CONCATENATED MODULE: ./src/components/Contextmenu.vue?vue&type=template&id=9e47a7a8&

// CONCATENATED MODULE: ./src/util.js
            function hasClass(el, className) {
                if (!className) {
                    return true;
                }
                if (!el || !el.className || typeof el.className !== 'string') {
                    return false;
                }
                for (let cn of el.className.split(/\s+/)) {
                    if (cn === className) {
                        return true;
                    }
                }
                return false;
            }

            function getElementsByClassName(className) {
                let els = [];
                for (let el of document.getElementsByClassName(className) || []) {
                    els.push(el);
                }
                return els;
            }

            function uuid() {
                return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
                    var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
                    return v.toString(16);
                });
            }


// CONCATENATED MODULE: ./src/constant.js
            const SUBMENU_X_OFFSET = 3;
            const SUBMENU_Y_OFFSET = -8;
            const SUBMENU_OPEN_TREND_LEFT = "left";
            const SUBMENU_OPEN_TREND_RIGHT = "right";
            const COMPONENT_NAME = "contextmenu-submenu";

// CONCATENATED MODULE: C:/Users/Laziji/AppData/Roaming/npm/node_modules/@vue/cli-service/node_modules/cache-loader/dist/cjs.js??ref--0-0!C:/Users/Laziji/AppData/Roaming/npm/node_modules/@vue/cli-service/node_modules/vue-loader/lib??vue-loader-options!./src/components/Contextmenu.vue?vue&type=script&lang=js&
//
//
//
//




            /* harmony default export */ var Contextmenuvue_type_script_lang_js_ = ({
                data() {
                    return {
                        items: [],
                        position: {
                            x: 0,
                            y: 0
                        },
                        style: {
                            zIndex: 2,
                            minWidth: 150
                        },
                        mainMenuInstance: null,
                        customClass: null,
                        mouseListening: false
                    };
                },
                mounted() {
                    const SubmenuConstructor = external_commonjs_vue_commonjs2_vue_root_Vue_default.a.component(COMPONENT_NAME);
                    this.mainMenuInstance = new SubmenuConstructor();
                    this.mainMenuInstance.items = this.items;
                    this.mainMenuInstance.commonClass = {
                        menu: this.$style.menu,
                        menuItem: this.$style.menu_item,
                        clickableMenuItem: this.$style.menu_item__clickable,
                        unclickableMenuItem: this.$style.menu_item__unclickable
                    };
                    this.mainMenuInstance.position = {
                        x: this.position.x,
                        y: this.position.y,
                        width: 0,
                        height: 0
                    };
                    this.mainMenuInstance.style.minWidth = this.style.minWidth;
                    this.mainMenuInstance.style.zIndex = this.style.zIndex;
                    this.mainMenuInstance.customClass = this.customClass;
                    this.mainMenuInstance.$mount();
                    document.body.appendChild(this.mainMenuInstance.$el);
                    this.addListener();
                },
                destroyed() {
                    this.removeListener();
                    if (this.mainMenuInstance) {
                        this.mainMenuInstance.close();
                    }
                },
                methods: {
                    mousewheelListener() {
                        this.$destroy();
                    },
                    mouseDownListener(e) {
                        let el = e.target;
                        const menus = getElementsByClassName(this.$style.menu);
                        while (!menus.find(m => m === el) && el.parentElement) {
                            el = el.parentElement;
                        }
                        if (!menus.find(m => m === el)) {
                            this.$destroy();
                        }
                    },
                    mouseClickListener(e) {
                        let el = e.target;
                        const menus = getElementsByClassName(this.$style.menu);
                        const menuItems = getElementsByClassName(this.$style.menu_item);
                        const unclickableMenuItems = getElementsByClassName(
                            this.$style.menu_item__unclickable
                        );
                        while (
                            !menus.find(m => m === el) &&
                        !menuItems.find(m => m === el) &&
                        el.parentElement
                    ) {
                            el = el.parentElement;
                        }
                        if (menuItems.find(m => m === el)) {
                            if (e.button !== 0 || unclickableMenuItems.find(m => m === el)) {
                                return;
                            }
                            this.$destroy();
                            return;
                        }
                        if (!menus.find(m => m === el)) {
                            this.$destroy();
                        }
                    },
                    addListener() {
                        if (!this.mouseListening) {
                            document.addEventListener("click", this.mouseClickListener);
                            document.addEventListener("mousedown", this.mouseDownListener);
                            document.addEventListener("mousewheel", this.mousewheelListener);
                            this.mouseListening = true;
                        }
                    },
                    removeListener() {
                        if (this.mouseListening) {
                            document.removeEventListener("click", this.mouseClickListener);
                            document.removeEventListener("mousedown", this.mouseDownListener);
                            document.removeEventListener("mousewheel", this.mousewheelListener);
                            this.mouseListening = false;
                        }
                    }
                }
            });

// CONCATENATED MODULE: ./src/components/Contextmenu.vue?vue&type=script&lang=js&
            /* harmony default export */ var components_Contextmenuvue_type_script_lang_js_ = (Contextmenuvue_type_script_lang_js_);
// EXTERNAL MODULE: ./src/components/Contextmenu.vue?vue&type=style&index=0&module=true&lang=css&
            var Contextmenuvue_type_style_index_0_module_true_lang_css_ = __webpack_require__("6187");

// CONCATENATED MODULE: C:/Users/Laziji/AppData/Roaming/npm/node_modules/@vue/cli-service/node_modules/vue-loader/lib/runtime/componentNormalizer.js
            /* globals __VUE_SSR_CONTEXT__ */

// IMPORTANT: Do NOT use ES2015 features in this file (except for modules).
// This module is a runtime utility for cleaner component module output and will
// be included in the final webpack user bundle.

            function normalizeComponent (
                scriptExports,
                render,
                staticRenderFns,
                functionalTemplate,
                injectStyles,
                scopeId,
                moduleIdentifier, /* server only */
                shadowMode /* vue-cli only */
            ) {
                // Vue.extend constructor export interop
                var options = typeof scriptExports === 'function'
                    ? scriptExports.options
                    : scriptExports

                // render functions
                if (render) {
                    options.render = render
                    options.staticRenderFns = staticRenderFns
                    options._compiled = true
                }

                // functional template
                if (functionalTemplate) {
                    options.functional = true
                }

                // scopedId
                if (scopeId) {
                    options._scopeId = 'data-v-' + scopeId
                }

                var hook
                if (moduleIdentifier) { // server build
                    hook = function (context) {
                        // 2.3 injection
                        context =
                            context || // cached call
                            (this.$vnode && this.$vnode.ssrContext) || // stateful
                            (this.parent && this.parent.$vnode && this.parent.$vnode.ssrContext) // functional
                        // 2.2 with runInNewContext: true
                        if (!context && typeof __VUE_SSR_CONTEXT__ !== 'undefined') {
                            context = __VUE_SSR_CONTEXT__
                        }
                        // inject component styles
                        if (injectStyles) {
                            injectStyles.call(this, context)
                        }
                        // register component module identifier for async chunk inferrence
                        if (context && context._registeredComponents) {
                            context._registeredComponents.add(moduleIdentifier)
                        }
                    }
                    // used by ssr in case component is cached and beforeCreate
                    // never gets called
                    options._ssrRegister = hook
                } else if (injectStyles) {
                    hook = shadowMode
                        ? function () { injectStyles.call(this, this.$root.$options.shadowRoot) }
                        : injectStyles
                }

                if (hook) {
                    if (options.functional) {
                        // for template-only hot-reload because in that case the render fn doesn't
                        // go through the normalizer
                        options._injectStyles = hook
                        // register for functioal component in vue file
                        var originalRender = options.render
                        options.render = function renderWithStyleInjection (h, context) {
                            hook.call(context)
                            return originalRender(h, context)
                        }
                    } else {
                        // inject component registration as beforeCreate hook
                        var existing = options.beforeCreate
                        options.beforeCreate = existing
                            ? [].concat(existing, hook)
                            : [hook]
                    }
                }

                return {
                    exports: scriptExports,
                    options: options
                }
            }

// CONCATENATED MODULE: ./src/components/Contextmenu.vue








            function injectStyles (context) {

                this["$style"] = (Contextmenuvue_type_style_index_0_module_true_lang_css_["default"].locals || Contextmenuvue_type_style_index_0_module_true_lang_css_["default"])

            }

            /* normalize component */

            var component = normalizeComponent(
                components_Contextmenuvue_type_script_lang_js_,
                render,
                staticRenderFns,
                false,
                injectStyles,
                null,
                null

            )

            /* harmony default export */ var Contextmenu = (component.exports);
// CONCATENATED MODULE: C:/Users/Laziji/AppData/Roaming/npm/node_modules/@vue/cli-service/node_modules/cache-loader/dist/cjs.js?{"cacheDirectory":"node_modules/.cache/vue-loader","cacheIdentifier":"9fd667e4-vue-loader-template"}!C:/Users/Laziji/AppData/Roaming/npm/node_modules/@vue/cli-service/node_modules/vue-loader/lib/loaders/templateLoader.js??vue-loader-options!C:/Users/Laziji/AppData/Roaming/npm/node_modules/@vue/cli-service/node_modules/cache-loader/dist/cjs.js??ref--0-0!C:/Users/Laziji/AppData/Roaming/npm/node_modules/@vue/cli-service/node_modules/vue-loader/lib??vue-loader-options!./src/components/Submenu.vue?vue&type=template&id=57402592&scoped=true&
            var Submenuvue_type_template_id_57402592_scoped_true_render = function () {var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;return _c('transition',{attrs:{"name":"contextmenu-submenu-fade"}},[(_vm.visible)?_c('div',{ref:"menu",class:[_vm.commonClass.menu, 'menu', _vm.customClass],style:({left: _vm.style.left + 'px', top: _vm.style.top + 'px', minWidth: _vm.style.minWidth + 'px', zIndex: _vm.style.zIndex}),on:{"contextmenu":function (e){ return e.preventDefault(); }}},[_c('div',{staticClass:"menu_body"},[_vm._l((_vm.items),function(item,index){return [(!item.hidden)?[(item.disabled)?_c('div',{key:index,class:[
                    _vm.commonClass.menuItem, _vm.commonClass.unclickableMenuItem,
                    'menu_item', 'menu_item__disabled',
                    item.divided?'menu_item__divided':null
                ]},[(_vm.hasIcon)?_c('div',{staticClass:"menu_item_icon"},[(item.icon)?_c('i',{class:item.icon}):_vm._e()]):_vm._e(),_c('span',{staticClass:"menu_item_label"},[_vm._v(_vm._s(item.label))]),_c('div',{staticClass:"menu_item_expand_icon"})]):(item.children)?_c('div',{key:index,class:[
                    _vm.commonClass.menuItem, _vm.commonClass.unclickableMenuItem,
                    'menu_item', 'menu_item__available',
                    _vm.activeSubmenu.index===index? 'menu_item_expand':null,
                    item.divided?'menu_item__divided':null
                ],on:{"mouseenter":function ($event){ return _vm.enterItem($event,item,index); }}},[(_vm.hasIcon)?_c('div',{staticClass:"menu_item_icon"},[(item.icon)?_c('i',{class:item.icon}):_vm._e()]):_vm._e(),_c('span',{staticClass:"menu_item_label"},[_vm._v(_vm._s(item.label))]),_c('div',{staticClass:"menu_item_expand_icon"},[_vm._v("▶")])]):_c('div',{key:index,class:[
                    _vm.commonClass.menuItem, _vm.commonClass.clickableMenuItem,
                    'menu_item', 'menu_item__available',
                    item.divided?'menu_item__divided':null
                ],on:{"mouseenter":function ($event){ return _vm.enterItem($event,item,index); },"click":function($event){return _vm.itemClick(item)}}},[(_vm.hasIcon)?_c('div',{staticClass:"menu_item_icon"},[(item.icon)?_c('i',{class:item.icon}):_vm._e()]):_vm._e(),_c('span',{staticClass:"menu_item_label"},[_vm._v(_vm._s(item.label))]),_c('div',{staticClass:"menu_item_expand_icon"})])]:_vm._e()]})],2)]):_vm._e()])}
            var Submenuvue_type_template_id_57402592_scoped_true_staticRenderFns = []


// CONCATENATED MODULE: ./src/components/Submenu.vue?vue&type=template&id=57402592&scoped=true&

// CONCATENATED MODULE: C:/Users/Laziji/AppData/Roaming/npm/node_modules/@vue/cli-service/node_modules/cache-loader/dist/cjs.js??ref--0-0!C:/Users/Laziji/AppData/Roaming/npm/node_modules/@vue/cli-service/node_modules/vue-loader/lib??vue-loader-options!./src/components/Submenu.vue?vue&type=script&lang=js&
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//



            /* harmony default export */ var Submenuvue_type_script_lang_js_ = ({
                name: COMPONENT_NAME,
                data() {
                    return {
                        commonClass: {
                            menu: null,
                            menuItem: null,
                            clickableMenuItem: null,
                            unclickableMenuItem: null
                        },
                        activeSubmenu: {
                            index: null,
                            instance: null
                        },
                        items: [],
                        position: {
                            x: 0,
                            y: 0,
                            width: 0,
                            height: 0
                        },
                        style: {
                            left: 0,
                            top: 0,
                            zIndex: 2,
                            minWidth: 150
                        },
                        customClass: null,
                        visible: false,
                        hasIcon: false,
                        openTrend: SUBMENU_OPEN_TREND_RIGHT
                    };
                },
                mounted() {
                    this.visible = true;
                    for (let item of this.items) {
                        if (item.icon) {
                            this.hasIcon = true;
                            break;
                        }
                    }
                    this.$nextTick(() => {
                        const windowWidth = document.documentElement.clientWidth;
                    const windowHeight = document.documentElement.clientHeight;
                    const menu = this.$refs.menu;
                    const menuWidth = menu.offsetWidth;
                    const menuHeight = menu.offsetHeight;

                    (this.openTrend === SUBMENU_OPEN_TREND_LEFT
                        ? this.leftOpen
                        : this.rightOpen)(windowWidth, windowHeight, menuWidth);

                    this.style.top = this.position.y;
                    if (this.position.y + menuHeight > windowHeight) {
                        if (this.position.height === 0) {
                            this.style.top = this.position.y - menuHeight;
                        } else {
                            this.style.top = windowHeight - menuHeight;
                        }
                    }
                });
                },
                methods: {
                    leftOpen(windowWidth, windowHeight, menuWidth) {
                        this.style.left = this.position.x - menuWidth;
                        this.openTrend = SUBMENU_OPEN_TREND_LEFT;
                        if (this.style.left < 0) {
                            this.openTrend = SUBMENU_OPEN_TREND_RIGHT;
                            if (this.position.width === 0) {
                                this.style.left = 0;
                            } else {
                                this.style.left = this.position.x + this.position.width;
                            }
                        }
                    },
                    rightOpen(windowWidth, windowHeight, menuWidth) {
                        this.style.left = this.position.x + this.position.width;
                        this.openTrend = SUBMENU_OPEN_TREND_RIGHT;
                        if (this.style.left + menuWidth > windowWidth) {
                            this.openTrend = SUBMENU_OPEN_TREND_LEFT;
                            if (this.position.width === 0) {
                                this.style.left = windowWidth - menuWidth;
                            } else {
                                this.style.left = this.position.x - menuWidth;
                            }
                        }
                    },
                    enterItem(e, item, index) {
                        if (!this.visible) {
                            return;
                        }
                        if (this.activeSubmenu.instance) {
                            if (this.activeSubmenu.index === index) {
                                return;
                            } else {
                                this.activeSubmenu.instance.close();
                                this.activeSubmenu.instance = null;
                                this.activeSubmenu.index = null;
                            }
                        }
                        if (!item.children) {
                            return;
                        }
                        const menuItemClientRect = e.target.getBoundingClientRect();
                        const SubmenuConstructor = external_commonjs_vue_commonjs2_vue_root_Vue_default.a.component(COMPONENT_NAME);
                        this.activeSubmenu.index = index;
                        this.activeSubmenu.instance = new SubmenuConstructor();
                        this.activeSubmenu.instance.items = item.children;
                        this.activeSubmenu.instance.openTrend = this.openTrend;
                        this.activeSubmenu.instance.commonClass = this.commonClass;
                        this.activeSubmenu.instance.position = {
                            x: menuItemClientRect.x + SUBMENU_X_OFFSET,
                            y: menuItemClientRect.y + SUBMENU_Y_OFFSET,
                            width: menuItemClientRect.width - 2 * SUBMENU_X_OFFSET,
                            height: menuItemClientRect.width
                        };
                        this.activeSubmenu.instance.style.minWidth =
                            typeof item.minWidth === "number" ? item.minWidth : this.style.minWidth;
                        this.activeSubmenu.instance.style.zIndex = this.style.zIndex;
                        this.activeSubmenu.instance.customClass =
                            typeof item.customClass === "string"
                                ? item.customClass
                                : this.customClass;
                        this.activeSubmenu.instance.$mount();
                        document.body.appendChild(this.activeSubmenu.instance.$el);
                    },
                    itemClick(item) {
                        if (!this.visible) {
                            return;
                        }
                        if (
                            item &&
                            !item.disabled &&
                            !item.hidden &&
                            typeof item.onClick === "function"
                        ) {
                            return item.onClick();
                        }
                    },
                    close() {
                        this.visible = false;
                        if (this.activeSubmenu.instance) {
                            this.activeSubmenu.instance.close();
                        }
                        this.$nextTick(() => {
                            this.$destroy();
                    });
                    }
                }
            });

// CONCATENATED MODULE: ./src/components/Submenu.vue?vue&type=script&lang=js&
            /* harmony default export */ var components_Submenuvue_type_script_lang_js_ = (Submenuvue_type_script_lang_js_);
// EXTERNAL MODULE: ./src/components/Submenu.vue?vue&type=style&index=0&id=57402592&scoped=true&lang=css&
            var Submenuvue_type_style_index_0_id_57402592_scoped_true_lang_css_ = __webpack_require__("cb42");

// EXTERNAL MODULE: ./src/components/Submenu.vue?vue&type=style&index=1&lang=css&
            var Submenuvue_type_style_index_1_lang_css_ = __webpack_require__("d66e");

// CONCATENATED MODULE: ./src/components/Submenu.vue







            /* normalize component */

            var Submenu_component = normalizeComponent(
                components_Submenuvue_type_script_lang_js_,
                Submenuvue_type_template_id_57402592_scoped_true_render,
                Submenuvue_type_template_id_57402592_scoped_true_staticRenderFns,
                false,
                null,
                "57402592",
                null

            )

            /* harmony default export */ var Submenu = (Submenu_component.exports);
// CONCATENATED MODULE: ./src/index.js






            const ContextmenuConstructor = external_commonjs_vue_commonjs2_vue_root_Vue_default.a.extend(Contextmenu);
            external_commonjs_vue_commonjs2_vue_root_Vue_default.a.component(COMPONENT_NAME, Submenu);

            function install(Vue) {
                let lastInstance = null;
                const ContextmenuProxy = function (options) {
                    let instance = new ContextmenuConstructor();
                    instance.items = options.items;
                    instance.position.x = options.x || 0;
                    instance.position.y = options.y || 0;
                    if (options.event) {
                        instance.position.x = options.event.clientX;
                        instance.position.y = options.event.clientY;
                    }
                    instance.customClass = options.customClass;
                    options.minWidth && (instance.style.minWidth = options.minWidth);
                    options.zIndex && (instance.style.zIndex = options.zIndex);
                    ContextmenuProxy.destroy();
                    lastInstance = instance;
                    instance.$mount();
                }
                ContextmenuProxy.destroy = function () {
                    if (lastInstance) {
                        lastInstance.$destroy();
                        lastInstance = null;
                    }
                }
                Vue.prototype.$contextmenu = ContextmenuProxy;
            }

            if (window && window.Vue) {
                install(window.Vue)
            }

            /* harmony default export */ var src = ({
                install
            });

// CONCATENATED MODULE: ./index.js


            /* harmony default export */ var index_0 = (src);
// CONCATENATED MODULE: C:/Users/Laziji/AppData/Roaming/npm/node_modules/@vue/cli-service/lib/commands/build/entry-lib.js


            /* harmony default export */ var entry_lib = __webpack_exports__["default"] = (index_0);



            /***/ }),

        /***/ "8bbf":
        /***/ (function(module, exports) {

            module.exports = __WEBPACK_EXTERNAL_MODULE__8bbf__;

            /***/ }),

        /***/ "903b":
        /***/ (function(module, exports, __webpack_require__) {

// Imports
            var ___CSS_LOADER_API_IMPORT___ = __webpack_require__("4210");
            exports = ___CSS_LOADER_API_IMPORT___(false);
// Module
            exports.push([module.i, ".menu[data-v-57402592]{position:fixed;box-shadow:0 2px 4px rgba(0,0,0,.12),0 0 6px rgba(0,0,0,.04);background:#fff;border-radius:4px;padding:8px 0}.menu_body[data-v-57402592]{display:block}.menu_item[data-v-57402592]{list-style:none;line-height:32px;padding:0 16px;margin:0;font-size:13px;outline:0;display:-webkit-box;display:flex;-webkit-box-align:center;align-items:center;-webkit-transition:.2s;transition:.2s;border-bottom:1px solid transparent}.menu_item__divided[data-v-57402592]{border-bottom-color:#ebeef5}.menu_item .menu_item_icon[data-v-57402592]{margin-right:8px;width:13px}.menu_item .menu_item_label[data-v-57402592]{-webkit-box-flex:1;flex:1}.menu_item .menu_item_expand_icon[data-v-57402592]{margin-left:16px;font-size:6px;width:10px}.menu_item__available[data-v-57402592]{color:#606266;cursor:pointer}.menu_item__available[data-v-57402592]:hover{background:#ecf5ff;color:#409eff}.menu_item__disabled[data-v-57402592]{color:#c0c4cc;cursor:not-allowed}.menu_item_expand[data-v-57402592]{background:#ecf5ff;color:#409eff}", ""]);
// Exports
            module.exports = exports;


            /***/ }),

        /***/ "c172":
        /***/ (function(module, exports, __webpack_require__) {

// style-loader: Adds some css to the DOM by adding a <style> tag

// load the styles
            var content = __webpack_require__("19dd");
            if(typeof content === 'string') content = [[module.i, content, '']];
            if(content.locals) module.exports = content.locals;
// add the styles to the DOM
            var add = __webpack_require__("3cc6").default
            var update = add("5838a5ca", content, true, {"sourceMap":false,"shadowMode":false});

            /***/ }),

        /***/ "cb42":
        /***/ (function(module, __webpack_exports__, __webpack_require__) {

            "use strict";
            /* harmony import */ var _C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_vue_style_loader_index_js_ref_6_oneOf_1_0_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_css_loader_dist_cjs_js_ref_6_oneOf_1_1_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_vue_loader_lib_loaders_stylePostLoader_js_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_postcss_loader_src_index_js_ref_6_oneOf_1_2_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_postcss_loader_src_index_js_ref_6_oneOf_1_3_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_cache_loader_dist_cjs_js_ref_0_0_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_vue_loader_lib_index_js_vue_loader_options_Submenu_vue_vue_type_style_index_0_id_57402592_scoped_true_lang_css___WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__("2d58");
            /* harmony import */ var _C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_vue_style_loader_index_js_ref_6_oneOf_1_0_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_css_loader_dist_cjs_js_ref_6_oneOf_1_1_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_vue_loader_lib_loaders_stylePostLoader_js_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_postcss_loader_src_index_js_ref_6_oneOf_1_2_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_postcss_loader_src_index_js_ref_6_oneOf_1_3_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_cache_loader_dist_cjs_js_ref_0_0_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_vue_loader_lib_index_js_vue_loader_options_Submenu_vue_vue_type_style_index_0_id_57402592_scoped_true_lang_css___WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_vue_style_loader_index_js_ref_6_oneOf_1_0_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_css_loader_dist_cjs_js_ref_6_oneOf_1_1_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_vue_loader_lib_loaders_stylePostLoader_js_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_postcss_loader_src_index_js_ref_6_oneOf_1_2_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_postcss_loader_src_index_js_ref_6_oneOf_1_3_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_cache_loader_dist_cjs_js_ref_0_0_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_vue_loader_lib_index_js_vue_loader_options_Submenu_vue_vue_type_style_index_0_id_57402592_scoped_true_lang_css___WEBPACK_IMPORTED_MODULE_0__);
            /* unused harmony reexport * */
            /* unused harmony default export */ var _unused_webpack_default_export = (_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_vue_style_loader_index_js_ref_6_oneOf_1_0_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_css_loader_dist_cjs_js_ref_6_oneOf_1_1_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_vue_loader_lib_loaders_stylePostLoader_js_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_postcss_loader_src_index_js_ref_6_oneOf_1_2_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_postcss_loader_src_index_js_ref_6_oneOf_1_3_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_cache_loader_dist_cjs_js_ref_0_0_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_vue_loader_lib_index_js_vue_loader_options_Submenu_vue_vue_type_style_index_0_id_57402592_scoped_true_lang_css___WEBPACK_IMPORTED_MODULE_0___default.a);

            /***/ }),

        /***/ "d66e":
        /***/ (function(module, __webpack_exports__, __webpack_require__) {

            "use strict";
            /* harmony import */ var _C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_vue_style_loader_index_js_ref_6_oneOf_1_0_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_css_loader_dist_cjs_js_ref_6_oneOf_1_1_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_vue_loader_lib_loaders_stylePostLoader_js_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_postcss_loader_src_index_js_ref_6_oneOf_1_2_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_postcss_loader_src_index_js_ref_6_oneOf_1_3_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_cache_loader_dist_cjs_js_ref_0_0_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_vue_loader_lib_index_js_vue_loader_options_Submenu_vue_vue_type_style_index_1_lang_css___WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__("c172");
            /* harmony import */ var _C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_vue_style_loader_index_js_ref_6_oneOf_1_0_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_css_loader_dist_cjs_js_ref_6_oneOf_1_1_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_vue_loader_lib_loaders_stylePostLoader_js_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_postcss_loader_src_index_js_ref_6_oneOf_1_2_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_postcss_loader_src_index_js_ref_6_oneOf_1_3_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_cache_loader_dist_cjs_js_ref_0_0_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_vue_loader_lib_index_js_vue_loader_options_Submenu_vue_vue_type_style_index_1_lang_css___WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_vue_style_loader_index_js_ref_6_oneOf_1_0_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_css_loader_dist_cjs_js_ref_6_oneOf_1_1_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_vue_loader_lib_loaders_stylePostLoader_js_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_postcss_loader_src_index_js_ref_6_oneOf_1_2_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_postcss_loader_src_index_js_ref_6_oneOf_1_3_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_cache_loader_dist_cjs_js_ref_0_0_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_vue_loader_lib_index_js_vue_loader_options_Submenu_vue_vue_type_style_index_1_lang_css___WEBPACK_IMPORTED_MODULE_0__);
            /* unused harmony reexport * */
            /* unused harmony default export */ var _unused_webpack_default_export = (_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_vue_style_loader_index_js_ref_6_oneOf_1_0_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_css_loader_dist_cjs_js_ref_6_oneOf_1_1_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_vue_loader_lib_loaders_stylePostLoader_js_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_postcss_loader_src_index_js_ref_6_oneOf_1_2_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_postcss_loader_src_index_js_ref_6_oneOf_1_3_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_cache_loader_dist_cjs_js_ref_0_0_C_Users_Laziji_AppData_Roaming_npm_node_modules_vue_cli_service_node_modules_vue_loader_lib_index_js_vue_loader_options_Submenu_vue_vue_type_style_index_1_lang_css___WEBPACK_IMPORTED_MODULE_0___default.a);

            /***/ }),

        /***/ "e31d":
        /***/ (function(module, exports) {

// document.currentScript polyfill by Adam Miller

// MIT license

            (function(document){
                var currentScript = "currentScript",
                    scripts = document.getElementsByTagName('script'); // Live NodeList collection

                // If browser needs currentScript polyfill, add get currentScript() to the document object
                if (!(currentScript in document)) {
                    Object.defineProperty(document, currentScript, {
                        get: function(){

                            // IE 6-10 supports script readyState
                            // IE 10+ support stack trace
                            try { throw new Error(); }
                            catch (err) {

                                // Find the second match for the "at" string to get file src url from stack.
                                // Specifically works with the format of stack traces in IE.
                                var i, res = ((/.*at [^\(]*\((.*):.+:.+\)$/ig).exec(err.stack) || [false])[1];

                                // For all scripts on the page, if src matches or if ready state is interactive, return the script tag
                                for(i in scripts){
                                    if(scripts[i].src == res || scripts[i].readyState == "interactive"){
                                        return scripts[i];
                                    }
                                }

                                // If no match, return null
                                return null;
                            }
                        }
                    });
                }
            })(document);


            /***/ })

        /******/ });
});