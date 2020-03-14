$.getScript('https://www.gstatic.com/firebasejs/7.11.0/firebase-app.js', () => {
$.getScript('https://www.gstatic.com/firebasejs/7.11.0/firebase-auth.js', () => {
$.getScript('https://www.gstatic.com/firebasejs/7.11.0/firebase-functions.js', () => {

    var firebaseConfig = {
        apiKey: "AIzaSyA8CjyW4zT7jL46sG4rEOcCON7SNsDN_do",
        authDomain: "engineeringlabproject.firebaseapp.com",
        databaseURL: "https://engineeringlabproject.firebaseio.com",
        projectId: "engineeringlabproject",
        storageBucket: "engineeringlabproject.appspot.com",
        messagingSenderId: "523387019136",
        appId: "1:523387019136:web:e414700e2e6bb35e372b65",
        measurementId: "G-5XBXE4QJLC"
    };

    firebase.initializeApp(firebaseConfig);
    firebase.functions();


    var exampleCloudFunction = firebase.functions().httpsCallable('exampleCloudFunction');
    exampleCloudFunction({text: "Making a call to the Cloud Function"}).then((result) => {
      console.log(result.data.text);
    }).catch((error) => {
      var code = error.code;
      var message = error.message;
      var details = error.details;
      console.log("\nCode: " + code + "\nMessage: " + message + "\nDetails: " + details);

    });

});});});