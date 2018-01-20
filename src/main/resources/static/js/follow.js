function follow(){
    // url (required), options (optional)
    fetch('http://localhost:8080/topic/3/follow', {
            method: 'get'
    }).then(function(response) {
        console.log(response);
        if(response==="true"){
            document.getElementById("followBtn").change = "none";
            document.getElementById("unfollowBtn").style.display = "block";
        }else{
           alert("Error");   
        }
    }).catch(function(err) {
            // Error :(
    });
}
function unfollow(){
    // url (required), options (optional)
    fetch('http://localhost:8080/topic/3/unfollow', {
            method: 'get'
    }).then(function(response) {
        if(response==="true"){
            document.getElementById("unfollowBtn").change = "none";
            document.getElementById("followBtn").style.display = "block";
        }else{
           alert("Error");   
        }
    }).catch(function(err) {
            // Error :(
    });
}