Dropzone.autoDiscover = false;

$(document).ready(function() {
    $('#pdfMerge').dropzone({
        maxFilesize: 25,
        acceptedFiles: ".pdf",
        init: function() {
            var _this = this;
            $('#btnMerge').click(function() {
                    _this.removeAllFiles();
                }
            );
        }
    })
});