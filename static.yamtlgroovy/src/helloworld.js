import {define} from "ace-builds";

define("ace/mode/helloworld_highlight_rules",["require","exports","module","ace/lib/oop","ace/mode/text_highlight_rules"], function(require, exports, module) {
"use strict";

var oop = require("../lib/oop");
var TextHighlightRules = require("./text_highlight_rules").TextHighlightRules;

var helloworldHighlightRules = function() {
 
    var keywords = (
        ""
    );

    var builtinConstants = (
        ""
    );

    var builtinFunctions = (
        ""
    );

    var dataTypes = (
        ""
    );

    var keywordMapper = this.createKeywordMapper({
        "support.function": builtinFunctions,
        "keyword": keywords,
        "constant.language": builtinConstants,
        "storage.type": dataTypes
    }, "identifier", true);

    this.$rules = {
        "start" : [ {
            token : "text",
            regex : "\\s+"
        } ]
    };
    this.normalizeRules();
};

oop.inherits(helloworldHighlightRules, TextHighlightRules);

exports.helloworldHighlightRules = helloworldHighlightRules;
});

define("ace/mode/helloworld",["require","exports","module","ace/lib/oop","ace/mode/text","ace/mode/helloworld_highlight_rules"], function(require, exports, module) {
"use strict";

var oop = require("../lib/oop");
var TextMode = require("./text").Mode;
var helloworldHighlightRules = require("./helloworld_highlight_rules").helloworldHighlightRules;

var Mode = function() {
    this.HighlightRules = helloworldHighlightRules;
    this.$behaviour = this.$defaultBehaviour;
};
oop.inherits(Mode, TextMode);

(function() {
    this.$id = "ace/mode/helloworld";
    this.snippetFileId = "ace/snippets/helloworld";
}).call(Mode.prototype);

exports.Mode = Mode;

});
