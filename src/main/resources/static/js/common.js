// Current Date & Time
function updateDateTime(){

    const now = new Date();

    const date = now.toLocaleDateString();

    const time = now.toLocaleTimeString();

    const dateElement = document.getElementById("currentDate");
    const timeElement = document.getElementById("currentTime");

    if(dateElement){
        dateElement.innerHTML = date;
    }

    if(timeElement){
        timeElement.innerHTML = time;
    }

}

setInterval(updateDateTime,1000);

updateDateTime();