// 출처
// https://velog.io/@skygl/FCM-Spring-Boot%EB%A5%BC-%EC%82%AC%EC%9A%A9%ED%95%98%EC%97%AC-%EC%9B%B9-%ED%91%B8%EC%8B%9C-%EA%B8%B0%EB%8A%A5-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0#step-2---%EC%9B%B9-%EC%95%B1%EC%9D%98-%EA%B5%AC%EC%84%B1-%EC%8A%A4%EB%8B%88%ED%8E%AB-%EA%B0%80%EC%A0%B8%EC%98%A4%EA%B8%B0
// https://github.com/skygl/miniprojects-2019/commit/5a1b36872f463bf0f4081fcc8ca22bb64288b43a

const firebaseModule = (function () {
    async function init() {
        // Your web app's Firebase configuration
        if ('serviceWorker' in navigator) {
            window.addEventListener('load', function() {
                navigator.serviceWorker.register('/firebase-messaging-sw.js')
                    .then(registration => {
                        var firebaseConfig = {
                            apiKey: "AIzaSyCLO2HUF8Og8v1SqiVM8k7Fr279Zu69hSU",
                            authDomain: "starcoffee-a405a.firebaseapp.com",
                            projectId: "starcoffee-a405a",
                            storageBucket: "starcoffee-a405a.appspot.com",
                            messagingSenderId: "281601784095",
                            appId: "1:281601784095:web:2a9cfbe7e91adba572982a",
                            measurementId: "G-LR2CH922P6"
                        };
                        // Initialize Firebase
                        firebase.initializeApp(firebaseConfig);

                        // Show Notification Dialog
                        const messaging = firebase.messaging();
                        messaging.requestPermission()
                            .then(function() {
                                return messaging.getToken();
                            })
                            .then(async function(token) {
                                await fetch('/push/register', { method: 'post', body: token })
                                messaging.onMessage(payload => {
                                    const title = payload.notification.title
                                    const options = {
                                        body : payload.notification.body
                                    }
                                    navigator.serviceWorker.ready.then(registration => {
                                        registration.showNotification(title, options);
                                    })
                                })
                            })
                            .catch(function(err) {
                                console.log("Error Occured");
                            })
                    })
            })
        }
    }

    return {
        init: function () {
            init()
        }
    }
})()

firebaseModule.init()