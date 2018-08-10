var stompClient = null;

sessionStorage.setItem("authToken", null);
sessionStorage.setItem("orderId", null);
localStorage.setItem("authToken", null);
localStorage.setItem("orderId", null);

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
    var socket = new SockJS('/gs-guide-websocket?authToken=' + sessionStorage.getItem("authToken"));
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {

        var topicToSubscribe = sessionStorage.getItem("orderId");

        if(!topicToSubscribe) return;

        console.log('Connected: ' + frame);

        setConnected(true);

        $("#listening-to-order").text(" ====> Listening to order with id: " + topicToSubscribe);

        stompClient.subscribe("/topic/order/" + topicToSubscribe, function (greeting) {

            showGreeting(greeting.body);

            if(greeting.body == "Autenticacao Expirada") {
                console.log("Pedindo o refresh token");
                 refreshToken();

                //$("#listening-to-order").text("");
                //setTimeout(disconnect, 2000);
            }
        });
    }, function (error) {
        alert("Nao conseguiu conectar")
    });
}

function refreshToken(){
    $.post( "/refresh?token="+ sessionStorage.getItem("authToken"), function (data) {
        sessionStorage.setItem("authToken", data);
        localStorage.setItem("authToken", data);

        alert("Refresh token realizado!");
    } );

}


function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
        var origem = $("#origem").val();

        $.post( "/orders", { origem:origem }, function (data) {
            var dataSplitted = data.split(">><<");
            var origin = dataSplitted[0];
            var orderId = dataSplitted[1];
            var authToken = dataSplitted[2];

            sessionStorage.setItem("authToken", authToken);
            sessionStorage.setItem("orderId", orderId);

            localStorage.setItem("authToken", authToken);
            localStorage.setItem("orderId", orderId);

            alert("Order created!");
        } );

    });
});

