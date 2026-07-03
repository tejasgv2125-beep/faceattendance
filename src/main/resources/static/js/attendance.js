// =========================================
// Elements
// =========================================

const video = document.getElementById("video");
const canvas = document.getElementById("canvas");

const studentPhoto = document.getElementById("studentPhoto");
const studentName = document.getElementById("studentName");
const studentUSN = document.getElementById("studentUSN");

const attendanceStatus = document.getElementById("attendanceStatus");
const cameraStatus = document.getElementById("cameraStatus");

// =========================================
// Variables
// =========================================

let stream;
let registeredStudents = [];
let faceMatcher = null;

// Prevent duplicate attendance requests
let lastRecognizedStudent = null;

// =========================================
// Load Face API Models
// =========================================

async function loadModels() {

    cameraStatus.innerHTML = "Loading AI Models...";

    await faceapi.nets.tinyFaceDetector.loadFromUri("/models");
    await faceapi.nets.faceLandmark68Net.loadFromUri("/models");
    await faceapi.nets.faceRecognitionNet.loadFromUri("/models");

    cameraStatus.innerHTML = "AI Models Loaded";
}

// =========================================
// Load Registered Students
// =========================================

async function loadRegisteredStudents() {

    cameraStatus.innerHTML = "Loading Registered Students...";

    const response = await fetch("/api/recognition/students");

    if (!response.ok) {
        throw new Error("Unable to load registered students.");
    }

    const students = await response.json();

    registeredStudents = students;

    const labeledDescriptors = [];

    students.forEach(student => {

        if (
            student.faceDescriptor == null ||
            student.faceDescriptor.trim() === ""
        ) {
            return;
        }

        const descriptorArray = JSON.parse(student.faceDescriptor);

        const descriptor = new Float32Array(descriptorArray);

        labeledDescriptors.push(

            new faceapi.LabeledFaceDescriptors(
                student.id.toString(),
                [descriptor]
            )

        );

    });

    faceMatcher = new faceapi.FaceMatcher(
        labeledDescriptors,
        0.55
    );

    cameraStatus.innerHTML =
        "Loaded " + labeledDescriptors.length + " Registered Students";
}

// =========================================
// Start Camera
// =========================================

async function startCamera() {

    try {

        stream = await navigator.mediaDevices.getUserMedia({

            video: {
                width: 1280,
                height: 720,
                facingMode: "user"
            },

            audio: false

        });

        video.srcObject = stream;

        cameraStatus.innerHTML = "Camera Ready";

    }

    catch (error) {

        console.error(error);

        cameraStatus.innerHTML = "Unable to access camera";

    }

}

// =========================================
// Mark Attendance
// =========================================

async function markAttendance(student) {

    if (lastRecognizedStudent === student.id) {
        return;
    }

    lastRecognizedStudent = student.id;

    try {

        const response = await fetch(
            "/api/attendance/mark/" + student.id,
            {
                method: "POST"
            }
        );

        const message = await response.text();

        if (response.ok) {

            attendanceStatus.className = "alert alert-success";
            attendanceStatus.innerHTML = message;

        } else {

            attendanceStatus.className = "alert alert-danger";
            attendanceStatus.innerHTML = message;

        }

    }

    catch (error) {

        console.error(error);

        attendanceStatus.className = "alert alert-danger";
        attendanceStatus.innerHTML = "Unable to mark attendance.";

    }

}

// =========================================
// Live Face Recognition
// =========================================

async function startRecognition() {

    setInterval(async () => {

        if (!faceMatcher) return;

        const detection = await faceapi
            .detectSingleFace(
                video,
                new faceapi.TinyFaceDetectorOptions()
            )
            .withFaceLandmarks()
            .withFaceDescriptor();

        // No Face

        if (!detection) {

            attendanceStatus.className = "alert alert-warning";
            attendanceStatus.innerHTML = "No Face Detected";

            studentPhoto.src = "https://via.placeholder.com/180";

            studentName.innerHTML = "Waiting...";
            studentUSN.innerHTML = "----";

            lastRecognizedStudent = null;

            return;
        }

        // Compare

        const bestMatch = faceMatcher.findBestMatch(
            detection.descriptor
        );

        // Unknown

        if (bestMatch.label === "unknown") {

            attendanceStatus.className = "alert alert-danger";
            attendanceStatus.innerHTML = "Unknown Face";

            studentPhoto.src = "https://via.placeholder.com/180";

            studentName.innerHTML = "Unknown";
            studentUSN.innerHTML = "----";

            lastRecognizedStudent = null;

            return;
        }

        // Find Student

        const student = registeredStudents.find(
            s => s.id.toString() === bestMatch.label
        );

        if (!student) return;

        // Update UI

        studentPhoto.src = student.photoPath;
        studentName.innerHTML = student.name;
        studentUSN.innerHTML = student.usn;

        // Mark Attendance

        await markAttendance(student);

    }, 1000);

}

// =========================================
// Initialize
// =========================================

(async () => {

    try {

        await loadModels();

        await loadRegisteredStudents();

        await startCamera();

        startRecognition();

        attendanceStatus.className = "alert alert-success";
        attendanceStatus.innerHTML = "Recognition Started";

    }

    catch (error) {

        console.error(error);

        attendanceStatus.className = "alert alert-danger";
        attendanceStatus.innerHTML = error.message;

    }

})();