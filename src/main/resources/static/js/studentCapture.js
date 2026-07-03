console.log("FaceAPI object:", window.faceapi);
const video = document.getElementById("video");
const canvas = document.getElementById("canvas");
const preview = document.getElementById("preview");

const captureBtn = document.getElementById("captureBtn");
const retakeBtn = document.getElementById("retakeBtn");

const hiddenImage = document.getElementById("capturedImage");
const hiddenDescriptor = document.getElementById("faceDescriptor");

const cameraStatus = document.getElementById("cameraStatus");

let stream;

// =========================================
// Load Face API Models
// =========================================

async function loadModels() {

    cameraStatus.innerHTML = "Loading AI Models...";
    cameraStatus.className = "badge bg-warning";

    await faceapi.nets.tinyFaceDetector.loadFromUri("/models");
    await faceapi.nets.faceLandmark68Net.loadFromUri("/models");
    await faceapi.nets.faceRecognitionNet.loadFromUri("/models");

    cameraStatus.innerHTML = "AI Models Loaded";
    cameraStatus.className = "badge bg-success";
}

// =========================================
// Start Camera
// =========================================

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

        cameraStatus.innerHTML = "Camera Ready";
        cameraStatus.className = "badge bg-success";

    }

    catch (error) {

        console.error(error);

        cameraStatus.innerHTML = "Camera Permission Denied";
        cameraStatus.className = "badge bg-danger";

    }

}

// =========================================
// Initialize
// =========================================

(async () => {

    try {

        await loadModels();

        await startCamera();

    }

    catch (e) {

        console.error(e);

        cameraStatus.innerHTML = "Failed to Load Models";
        cameraStatus.className = "badge bg-danger";

    }

})();

// =========================================
// Capture Student
// =========================================

captureBtn.addEventListener("click", async () => {

    cameraStatus.innerHTML = "Detecting Face...";
    cameraStatus.className = "badge bg-warning";

    const detection = await faceapi
        .detectSingleFace(
            video,
            new faceapi.TinyFaceDetectorOptions()
        )
        .withFaceLandmarks()
        .withFaceDescriptor();

    if (!detection) {

        alert("No face detected.\n\nPlease look directly at the camera.");

        cameraStatus.innerHTML = "No Face Detected";
        cameraStatus.className = "badge bg-danger";

        return;
    }

    // Capture Image

    const context = canvas.getContext("2d");

    context.drawImage(video, 0, 0, canvas.width, canvas.height);

    const image = canvas.toDataURL("image/jpeg", 0.9);

    preview.src = image;

    preview.style.display = "block";

    hiddenImage.value = image;

    // Save Face Descriptor

    const descriptor = Array.from(detection.descriptor);

    hiddenDescriptor.value = JSON.stringify(descriptor);

    video.style.display = "none";

    if (stream) {

        stream.getTracks().forEach(track => track.stop());

    }

    cameraStatus.innerHTML = "Student Registered Successfully";
    cameraStatus.className = "badge bg-success";

});

// =========================================
// Retake
// =========================================

retakeBtn.addEventListener("click", async () => {

    preview.style.display = "none";

    video.style.display = "block";

    hiddenImage.value = "";

    hiddenDescriptor.value = "";

    cameraStatus.innerHTML = "Restarting Camera...";
    cameraStatus.className = "badge bg-warning";

    await startCamera();

});