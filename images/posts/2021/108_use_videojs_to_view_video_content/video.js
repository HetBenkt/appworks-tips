$(document).ready(function() {
    console.log("Ready!");

    //Parse URL parameters
	const queryString = window.location.search;
    console.log(queryString);
    const urlParams = new URLSearchParams(queryString);
    const format = urlParams.get('format');
    const itemId = urlParams.get('itemId');
    const posterURL = urlParams.get('posterURL');
    const name = urlParams.get('name');
    const params = {
        format,
        itemId,
        posterURL,
        name
    };
    console.log(params);

    //Setup the input data for the player (format and URL)
    var data = {
        format: params.format,
        url: '/home/appworks_tips/app/entityRestService/Items(' + params.itemId + ')/ContentStream'
    };

    //Build the player object
    var myPlayer = videojs("aw_video");
    myPlayer.src({
        type: 'video/' + data.format,
        src: data.url
    });

    //When ready set some defaults...
    myPlayer.ready(function() {
        console.log(myPlayer.muted(), myPlayer.volume());
        myPlayer.poster(params.posterURL + '&name=' + params.name);
        myPlayer.volume(0.01);
    });
});