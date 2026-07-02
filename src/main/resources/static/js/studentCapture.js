const video = document.getElementById("video");
const canvas = document.getElementById("canvas");
const preview = document.getElementById("preview");
const captureBtn = document.getElementById("captureBtn");
const retakeBtn = document.getElementById("retakeBtn");
const hiddenImage = document.getElementById("capturedImage");
const cameraStatus = document.getElementById("cameraStatus");

let stream;

// Start Camera
async function startCamera() {

    try {

        stream = await navigator.mediaDevices.getUserMedia({
            video: {
                width: 640,
                height: 480,
                facingMode: "user"
            },
            audio: false
        });

        video.srcObject = stream;

        cameraStatus.innerHTML = "✅ Camera Ready";
        cameraStatus.className = "badge bg-success";

    } catch (error) {

        console.error(error);

        cameraStatus.innerHTML = "❌ Camera Permission Denied";
        cameraStatus.className = "badge bg-danger";
    }
}

startCamera();

// Capture Photo
captureBtn.addEventListener("click", () => {

    const context = canvas.getContext("2d");

    context.drawImage(video, 0, 0, canvas.width, canvas.height);

    const image = canvas.toDataURL("image/jpeg", 0.9);

    preview.src = image;
    preview.style.display = "block";

    hiddenImage.value = image;

    video.style.display = "none";

    cameraStatus.innerHTML = "📸 Photo Captured";
    cameraStatus.className = "badge bg-primary";

    // Stop camera after capture
    if (stream) {

        stream.getTracks().forEach(track => track.stop());

    }

});

// Retake Photo
retakeBtn.addEventListener("click", () => {

    preview.style.display = "none";

    video.style.display = "block";

    hiddenImage.value = "";

    cameraStatus.innerHTML = "🔄 Restarting Camera...";
    cameraStatus.className = "badge bg-warning text-dark";

    startCamera();

});