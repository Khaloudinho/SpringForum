function follow () {
  var topicId = document.getElementById('idTopic').value
  var userId = document.getElementById('idUser').value
  const url = `http://localhost:8080/topic/${topicId}/follow`
  axios.get(url).then(resp => {
    console.log(resp)
  })
  // fetch(url, {
  //   method: 'get'
  // }).then(function (response) {
  //   document.getElementById('followBtn').change = 'none'
  //   document.getElementById('unfollowBtn').style.display = 'block'
  // }).catch(function (err) {
  //   alert('Error')
  // })
}

function unfollow () {
  var topicId = document.getElementById('idTopic').value
  var userId = document.getElementById('idUser').value
  // url (required), options (optional)
  var url = 'http://localhost:8080/topic/' + topicId + '/unfollow'
  // fetch(url, {
  //   method: 'get'
  // }).then(function (response) {
  //   document.getElementById('unfollowBtn').change = 'none'
  //   document.getElementById('followBtn').style.display = 'block'
  // }).catch(function (err) {
  //   alert('Error')
  // })
}
