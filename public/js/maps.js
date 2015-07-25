$(document).ready(function(){/* google maps -----------------------------------------------------*/
google.maps.event.addDomListener(window, 'load', initialize);


function initialize() {

    var mapOptions = {
        scrollWheel: false,
        zoom: 13
    };
    var map = new google.maps.Map(document.getElementById("map-canvas"), mapOptions);

    var userId = $("#userid").text()


    $.getJSON("/api/users/"+userId+"/location", function(data){
        var latlng = new google.maps.LatLng(data.latitude, data.longitude);

        var marker = new google.maps.Marker({
            position: latlng,
            url: '/',
            animation: google.maps.Animation.DROP
        });
        marker.setMap(map);
    });

  var kmlUrl = "https://s3-ap-southeast-1.amazonaws.com/50hackssg/dengue_clusters.kml"
  loadKmlLayer(kmlUrl,map);

};
function loadKmlLayer(src, map) {
  var kmlLayer = new google.maps.KmlLayer(src, {
    suppressInfoWindows: true,
    preserveViewport: false,
    map: map
  });
}
/* end google maps -----------------------------------------------------*/
});