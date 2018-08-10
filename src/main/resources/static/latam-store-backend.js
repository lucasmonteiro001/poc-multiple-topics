var stompClient = null;

var topicToSubscribe = "";

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/gs-guide-websocket?authToken=backend');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        topicToSubscribe = prompt("What's the ORDER_ID?");

        if(!topicToSubscribe) return;

        setConnected(true);

        console.log('Connected: ' + frame);

        console.warn(topicToSubscribe);
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("/app/update/" + topicToSubscribe, {}, JSON.stringify({'name': $("#name").val()}));
    $("#name").val("");
    $('.alert').show();

    setTimeout(function () {
        $('.alert').hide();
    }, 1000);

}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});

