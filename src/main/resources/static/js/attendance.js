// =========================================
// Elements
// =========================================

const video = document.getElementById("video");
const canvas = document.getElementById("canvas");

const studentPhoto = document.getElementById("studentPhoto");
const studentName = document.getElementById("studentName");
const studentUSN = document.getElementById("studentUSN");
const studentDepartment = document.getElementById("studentDepartment");
const studentSemester = document.getElementById("studentSemester");

const attendanceStatus = document.getElementById("attendanceStatus");

// =========================================
// Variables
// =========================================

let stream = null;
let registeredStudents = [];
let faceMatcher = null;

let lastRecognizedStudent = null;
let recognitionRunning = false;

// =========================================
// Load Face API Models
// =========================================

async function loadModels() {

    attendanceStatus.className = "alert alert-info";
    attendanceStatus.innerHTML = "Loading AI Models...";

    await faceapi.nets.tinyFaceDetector.loadFromUri("/models");
    await faceapi.nets.faceLandmark68Net.loadFromUri("/models");
    await faceapi.nets.faceRecognitionNet.loadFromUri("/models");

    attendanceStatus.className = "alert alert-success";
    attendanceStatus.innerHTML = "AI Models Loaded";

}

// =========================================
// Load Registered Students
// =========================================

async function loadRegisteredStudents() {

    attendanceStatus.className = "alert alert-info";
    attendanceStatus.innerHTML = "Loading Registered Students...";

    const response = await fetch("/api/recognition/students");

    if (!response.ok) {

        throw new Error("Unable to load registered students.");

    }

    registeredStudents = await response.json();

    const labeledDescriptors = [];

    registeredStudents.forEach(student => {

        if (
            !student.faceDescriptor ||
            student.faceDescriptor.trim() === ""
        ) {
            return;
        }

        const descriptor = new Float32Array(
            JSON.parse(student.faceDescriptor)
        );

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

    attendanceStatus.className = "alert alert-success";
    attendanceStatus.innerHTML =
        labeledDescriptors.length +
        " Students Loaded";

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

        await new Promise(resolve => {

            video.onloadedmetadata = () => {

                video.play();

                resolve();

            };

        });

        attendanceStatus.className = "alert alert-success";
        attendanceStatus.innerHTML = "Camera Ready";

    }

    catch (error) {

        console.error(error);

        attendanceStatus.className = "alert alert-danger";
        attendanceStatus.innerHTML =
            "Unable to access camera.";

        throw error;

    }

}
// =========================================
// Reset Student Information
// =========================================

function resetStudentInfo() {

    studentPhoto.src = "https://via.placeholder.com/180";

    studentName.innerHTML = "Waiting...";

    studentUSN.innerHTML = "----";

    studentDepartment.innerHTML = "Department";

    studentSemester.innerHTML = "Semester";

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

        }

        else {

            attendanceStatus.className = "alert alert-danger";

            attendanceStatus.innerHTML = message;

        }

    }

    catch (error) {

        console.error(error);

        attendanceStatus.className = "alert alert-danger";

        attendanceStatus.innerHTML =

            "Unable to mark attendance.";

    }

}

// =========================================
// Live Face Recognition
// =========================================

async function startRecognition() {

    if (recognitionRunning) return;

    recognitionRunning = true;

    setInterval(async () => {

        if (!faceMatcher) return;

        const detection = await faceapi

            .detectSingleFace(

                video,

                new faceapi.TinyFaceDetectorOptions()

            )

            .withFaceLandmarks()

            .withFaceDescriptor();

        // =====================================
        // No Face Detected
        // =====================================

        if (!detection) {

            attendanceStatus.className =

                "alert alert-warning";

            attendanceStatus.innerHTML =

                "Waiting for Face...";

            resetStudentInfo();

            lastRecognizedStudent = null;

            return;

        }

        // =====================================
        // Compare Face
        // =====================================

        const bestMatch = faceMatcher.findBestMatch(

            detection.descriptor

        );

        // =====================================
        // Unknown Person
        // =====================================

        if (bestMatch.label === "unknown") {

            attendanceStatus.className =

                "alert alert-danger";

            attendanceStatus.innerHTML =

                "Unknown Face";

            studentPhoto.src =

                "https://via.placeholder.com/180";

            studentName.innerHTML =

                "Unknown Person";

            studentUSN.innerHTML = "----";

            studentDepartment.innerHTML =

                "Department";

            studentSemester.innerHTML =

                "Semester";

            lastRecognizedStudent = null;

            return;

        }

        // =====================================
        // Find Student
        // =====================================

        const student = registeredStudents.find(

            s => s.id.toString() === bestMatch.label

        );

        if (!student) return;

        // =====================================
        // Update Student Information
        // =====================================

        studentPhoto.src = student.photoPath;

        studentName.innerHTML = student.name;

        studentUSN.innerHTML = student.usn;

        studentDepartment.innerHTML =

            student.department;

        studentSemester.innerHTML =

            student.semester;

        // =====================================
        // Mark Attendance
        // =====================================

        await markAttendance(student);

    }, 1000);

}
// =========================================
// Initialize Attendance Terminal
// =========================================

async function initializeAttendanceSystem() {

    try {

        // Reset UI

        resetStudentInfo();

        attendanceStatus.className = "alert alert-info";
        attendanceStatus.innerHTML = "Initializing Attendance Terminal...";

        // Load Face API Models

        await loadModels();

        // Load Registered Students

        await loadRegisteredStudents();

        // Start Camera

        await startCamera();

        // Start Recognition

        await startRecognition();

        attendanceStatus.className = "alert alert-success";
        attendanceStatus.innerHTML =
            "System Ready - Please Stand in Front of the Camera";

    }

    catch (error) {

        console.error("Initialization Error :", error);

        attendanceStatus.className = "alert alert-danger";

        attendanceStatus.innerHTML =
            "System Initialization Failed.";

    }

}

// =========================================
// Auto Reset Student Card
// =========================================

function autoResetCard() {

    setTimeout(() => {

        resetStudentInfo();

        attendanceStatus.className = "alert alert-warning";

        attendanceStatus.innerHTML =
            "Waiting for Face...";

        lastRecognizedStudent = null;

    }, 3000);

}

// =========================================
// Enhance markAttendance()
// =========================================
//
// After:
//
// attendanceStatus.innerHTML = message;
//
// add:
//
// autoResetCard();
//
// in BOTH success and failure blocks.
//
// Example:
//
// if(response.ok){
//
//      attendanceStatus.className="alert alert-success";
//
//      attendanceStatus.innerHTML=message;
//
//      autoResetCard();
//
// }
//
// else{
//
//      attendanceStatus.className="alert alert-danger";
//
//      attendanceStatus.innerHTML=message;
//
//      autoResetCard();
//
// }
//
// =========================================


// =========================================
// Start System
// =========================================

window.addEventListener("load", () => {

    initializeAttendanceSystem();

});