// const video = document.getElementById("video");
// const captureBtn = document.getElementById("captureBtn");
// const retakeBtn = document.getElementById("retakeBtn");
// const status = document.getElementById("status");
// const studentId = document.getElementById("studentId").value;
//
// let stream;
//
// // ===============================
// // Load AI Models
// // ===============================
//
// async function loadModels() {
//
//     status.className = "alert alert-info";
//     status.innerHTML = "Loading AI Models...";
//
//     await faceapi.nets.tinyFaceDetector.loadFromUri("/models");
//     await faceapi.nets.faceLandmark68Net.loadFromUri("/models");
//     await faceapi.nets.faceRecognitionNet.loadFromUri("/models");
//
//     status.className = "alert alert-success";
//     status.innerHTML = "AI Models Loaded Successfully";
//
// }
//
// // ===============================
// // Start Camera
// // ===============================
//
// async function startCamera() {
//
//     stream = await navigator.mediaDevices.getUserMedia({
//
//         video: {
//             width: 640,
//             height: 480,
//             facingMode: "user"
//         },
//
//         audio: false
//
//     });
//
//     video.srcObject = stream;
//
// }
//
// (async () => {
//
//     try {
//
//         await loadModels();
//
//         await startCamera();
//
//         status.className = "alert alert-success";
//         status.innerHTML = "Camera Ready";
//
//     }
//
//     catch (error) {
//
//         console.error(error);
//
//         status.className = "alert alert-danger";
//         status.innerHTML = "Unable to Start Camera";
//
//     }
//
// })();
//
// // ===============================
// // Register Face
// // ===============================
//
// captureBtn.addEventListener("click", async function () {
//
//     status.className = "alert alert-warning";
//     status.innerHTML = "Detecting Face...";
//
//     const detection = await faceapi
//         .detectSingleFace(
//             video,
//             new faceapi.TinyFaceDetectorOptions()
//         )
//         .withFaceLandmarks()
//         .withFaceDescriptor();
//
//     if (!detection) {
//
//         status.className = "alert alert-danger";
//         status.innerHTML = "No Face Detected";
//
//         return;
//
//     }
//
//     const descriptor = Array.from(detection.descriptor);
//     console.log("Student ID:", studentId);
//     console.log("Descriptor Length:", descriptor.length);
//     console.log(descriptor);
//
//     try {
//
//         const response = await fetch("/api/faces/register", {
//
//             method: "POST",
//
//             headers: {
//                 "Content-Type": "application/json"
//             },
//
//             body: JSON.stringify({
//
//                 studentId: Number(studentId),
//
//                 descriptor: descriptor
//
//             })
//
//         });
//
//         const message = await response.text();
//
//         if (response.ok) {
//
//             status.className = "alert alert-success";
//             status.innerHTML = "✅ " + message;
//
//         }
//
//         else {
//
//             status.className = "alert alert-danger";
//             status.innerHTML = message;
//
//         }
//
//     }
//
//     catch (error) {
//
//         console.error(error);
//
//         status.className = "alert alert-danger";
//         status.innerHTML = "Failed to Register Face";
//
//     }
//
// });
//
// // ===============================
// // Retry
// // ===============================
//
// retakeBtn.addEventListener("click", function () {
//
//     status.className = "alert alert-info";
//     status.innerHTML = "Ready for New Registration";
//
// });