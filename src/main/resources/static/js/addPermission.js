function addPermission(){
    var projectId = document.getElementById("idProject").value ;
    
    
    var e = document.getElementById("selectPermission");

    var userId = document.getElementById( "selectUser" );
    var  id =  userId.options[ userId.selectedIndex ].value;
    var  text=  userId.options[ userId.selectedIndex ].text;

    var permission = e.options[e.selectedIndex].value;
    var url = "http://localhost:8080/permission/"+projectId+"?user="+id+"&permission="+permission;

    fetch(url, {
            method: 'get'
    }).then(function(response) {
        if(permission==="WRITE"){
            createBlockUser(id,text,"WRITE");
        }else if(permission ==="READ"){
            createBlockUser(id,text,"READ");
        }else{
            createBlockUser(id,text,"WRITE");
            createBlockUser(id,text,"READ");
        }
    }).catch(function(err) {
        Alert("Error");
    });
}
function createBlockUser(id,text,permission){
    var notif = document.createElement("div");
    notif.className += " notification";
    var button = document.createElement("button");
    button.className += "delete";
    button.setAttribute("id", id);
    
    var node = document.createTextNode(text);

    if(permission==="WRITE"){
        var element=document.getElementById("listWriterUsers"); 
        button.setAttribute("onclick","deletePermission(this.id,'WRITE')");
    }else if(permission==="READ"){
        var element=document.getElementById("listReaderUsers");  
        button.setAttribute("onclick","deletePermission(this.id,'READ')");
    }
    
    notif.appendChild(button);
    notif.appendChild(node);
    
    element.appendChild(notif);
}

function deletePermission(id,permission)
{
    var projectId = document.getElementById("idProject").value ;
    
    var url = "http://localhost:8080/permission/"+projectId+"?user="+id+"&permission="+permission;
    alert(url);
    fetch(url, {
            method: 'delete'
    }).then(function(response) {
        if(permission==="WRITE"){
            deleteBlockUser(id,"WRITE");
        }else if(permission ==="READ"){
            deleteBlockUser(id,"READ");
        }
    }).catch(function(err) {
        Alert("Error");
    });
}

function deleteBlockUser(id,permission){
    if(permission==="WRITE"){
        var element = document.getElementById(id+"Reader");
    }else if(permission==="READ"){
        var element=document.getElementById(id+"Writer");  
    }
    element.parentNode.removeChild(element);
}