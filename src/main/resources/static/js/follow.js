function follow(){
    var topicId = document.getElementById("idTopic").value ;
    // url (required), options (optional)
    fetch('http://localhost:8080/topic/'+topicId+'/follow', {
            method: 'get'
    }).then(function(response) {
        document.getElementById("followBtn").change = "none";
        document.getElementById("unfollowBtn").style.display = "block";
    }).catch(function(err) {
        alert("Error");  
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