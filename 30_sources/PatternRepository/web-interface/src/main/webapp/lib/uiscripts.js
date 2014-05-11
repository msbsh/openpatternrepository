/**
 * @author cm
 */
$(document).ready( function() {
				
    $("#toc .hideArrow").toggle(
        function() {
            $("#toc ul").slideUp("slow");
        },
        function() {
            $("#toc ul").slideDown("slow");
        }
        );

    $('body').mousemove(updateTooltip);
				
});

$(function() {
    $(".categoryImg").click(function() {
        var content = $(this).parent().next("div");

        if (content.length == 0) {
            return;
        }

        if (content.is(":visible")) {
            $(this).attr("src", "images/rightArrow.png");
        } else {
            $(this).attr("src", "images/downArrow.png");
        }

        content.slideToggle();
    });

    $(".browseTag").click(function() {
        var tag = $(this).text();

        var resultOut = $("#tagsresults");

        // setting the heading
        resultOut.prev("h2").text("Patterns with tag: " + tag);

        resultOut.children().remove();

        $.getJSON("/withTag", {
            "q" : tag
        }, function(data) {
            for (var i = 0; i < data.length; i++) {
                var url = "wiki/" + data[i].slug;

                var a = document.createElement("a");
                a.href = url;
                a.title = "Get details about this pattern";
                $(a).text(data[i].name);

                resultOut[0].appendChild(a);
            }
        });



        return false;
    });

    $("#search\\:searchQuery").keypress(function(e) {
        if(e.which == 13){
            $("#search\\:searchButton").click();
            e.preventDefault();
            return false;
        }
    });
});

currentTooltip = null;
function updateTooltip(e) {
    if (currentTooltip != null && currentTooltip.style.display == 'block') {
        x = (e.pageX ? e.pageX : window.event.x) + currentTooltip.offsetParent.scrollLeft - currentTooltip.offsetParent.offsetLeft;
        y = (e.pageY ? e.pageY : window.event.y) + currentTooltip.offsetParent.scrollTop - currentTooltip.offsetParent.offsetTop;
        currentTooltip.style.left = (x + 20) + "px";
        currentTooltip.style.top   = (y + 20) + "px";
    }
}
function showTooltip(id) {
    currentTooltip = document.getElementById(id);
    currentTooltip.style.display = "block";
}
function hideTooltip() {
    currentTooltip.style.display = "none";
}

$(function() {
    $("#uploadedFilesDialog").hide();

    $("#showUploadedFilesDialog").click(function() {
        $("#uploadedFilesDialog").slideToggle();
        return false;
    });

    function fillDialog() {
        $()
    }
});