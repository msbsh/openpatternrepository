    
document.getElementById("content").addEventListener("mouseup",sendSelection,false);

function sendSelection(e) {

    if (!e) e = window.event;

    if ((e.button || e.which) != 1) return;

    var range = window.getSelection().getRangeAt(0);

    if (range.collapsed) return;

    
    var obj = document.getElementById("pasteContent");
    /* TODO  If a text is selected that lies completly outside of content than the whole text is selected */
    if ( (obj != range.startContainer) && (!contains(range.startContainer,obj))) {
        range.setStartBefore(obj);
    }
    if ((obj != range.endContainer) && !contains(range.endContainer,obj)) {
        range.setEndAfter(obj);
    }
    var div  = document.createElement("div");
    div.appendChild(range.cloneContents());
    document.getElementById("addWizardPatternForm:selection").value  = div.innerHTML;
    iceSubmitPartial(document.getElementById("addWizardPatternForm"),document.getElementById("addWizardPatternForm:selection"),e);
}
function contains(child,parent){

    if(typeof(child)=='string'){
        child=document.getElementById(child);
    }
    if(!child){
        return false;
    }

    if(child.parentNode===parent){
        return true;
    }
    if(child===document.body){
        return false;
    }
    return contains(child.parentNode,parent);
}
