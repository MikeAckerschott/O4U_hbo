import { ref } from 'vue'

const student_project = ref({
    "WoR-project": {
        id: 1, running: false, description: 'Description of WoR-project ', criteriaToReach: {
            "WoR-P-1": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for WoR-P-1", grade: "Goed" },
            "WoR-P-2": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for WoR-P-2", grade: "Voldoende" },
            "WoR-P-3": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for WoR-P-3", grade: "Goed" },
            "WoR-P-4": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for WoR-P-4", grade: "Goed" },
            "WoR-P-5": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for WoR-P-5", grade: "Voldoende" },
            "WoR-P-6": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for WoR-P-6", grade: "Goed" },
            "WoR-P-7": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for WoR-P-7", grade: "Goed" },
            "WoR-P-8": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for WoR-P-8", grade: "Onvoldoende" },
            "WoR-P-9": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for WoR-P-9", grade: "Goed" },
            "WoR-P-10": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for WoR-P-10", grade: "Goed" },
        }
    },
    "IoT-project": {
        id: 2, running: false, description: 'Description of IoT-project', criteriaToReach: {
            "INTOFT25-1": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for IoT-P-1", grade: "Goed" },
            "INTOFT25-2": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for IoT-P-2", grade: "Voldoende" },
            "INTOFT25-3": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for IoT-P-3", grade: "Voldoende" },
            "INTOFT25-4": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for IoT-P-3", grade: "Voldoende" },
            "INTOFT25-5": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for IoT-P-3", grade: "Voldoende" },
        }
    },
    "Intro ICT beroepsproduct": {
        id: 3, running: false, description: 'beroepsproduct', criteriaToReach: {
            "INTROICT-1": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback", grade: "Goed" },
            "INTROICT-2": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for IoT-P-1", grade: "Goed" },
            "INTROICT-3": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for IoT-P-1", grade: "Goed" },
        }
    },
    "OOPD Game": {
        id: 4, running: false, description: 'beroepsproduct', criteriaToReach: {
            "OOPD-1": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for IoT-P-1", grade: "Goed" },
            "OOPD-2": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for IoT-P-1", grade: "Goed" },
            "OOPD-3": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for IoT-P-1", grade: "Goed" },
            "OOPD-4": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for IoT-P-1", grade: "Goed" },
        }
    },
    "EPD Spoorwegovergang simulatie": {
        id: 4, running: false, description: 'beroepsproduct', criteriaToReach: {
            "EPD-1": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for IoT-P-1", grade: "Goed" },
            "EPD-2": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for IoT-P-1", grade: "Goed" },
            "EPD-3": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for IoT-P-1", grade: "Goed" },
            "EPD-4": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for IoT-P-1", grade: "Goed" },
            "EPD-7": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for IoT-P-1", grade: "Goed" },
            "EPD-8": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for IoT-P-1", grade: "Goed" },
        }
    },
    "DBRP - KPI's": {
        id: 4, running: false, description: 'beroepsproduct', criteriaToReach: {
            "DBRP-1": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for IoT-P-1", grade: "Goed" },
            "DBRP-2": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for IoT-P-1", grade: "Goed" },
            "DBRP-3": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for IoT-P-1", grade: "Goed" },
        }
    },
    "FAT Beroepsproduct": {
        id: 4, running: false, description: 'beroepsproduct', criteriaToReach: {
            "FAT-1": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for IoT-P-1", grade: "Goed" },
            "FAT-2": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for IoT-P-1", grade: "Goed" },
            "FAT-3": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for IoT-P-1", grade: "Goed" },
            "FAT-4": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for IoT-P-1", grade: "Goed" },
            "FAT-5": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for IoT-P-1", grade: "Goed" },
        }
    },
    "SPAD Game": {
        id: 4, running: false, description: 'beroepsproduct', criteriaToReach: {
            "SPAD-1": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for IoT-P-1", grade: "Goed" },
            "SPAD-2": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for IoT-P-1", grade: "Goed" },
            "SPAD-3": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for IoT-P-1", grade: "Goed" },
            "SPAD-4": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for IoT-P-1", grade: "Goed" },
        }
    },
    "SPB Huiswerkopdrachten": {
        id: 4, running: false, description: 'beroepsproduct', criteriaToReach: {
            "SPB-1": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for IoT-P-1", grade: "Goed" },
            "SPB-2": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for IoT-P-1", grade: "Goed" },
            "SPB-3": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for IoT-P-1", grade: "Goed" },
            "SPB-4": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for IoT-P-1", grade: "Goed" },
            "SPB-5": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for IoT-P-1", grade: "Goed" },
            "SPB-6": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for IoT-P-1", grade: "Goed" },
        }
    },
    "WTUX Fletnix": {
        id: 4, running: false, description: 'beroepsproduct', criteriaToReach: {
            "WTUX-1": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for IoT-P-1", grade: "Goed" },
            "WTUX-2": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for IoT-P-1", grade: "Goed" },
            "WTUX-3": { studentVerantwoording: "Ik heb deze criteria behaald door...", feedback: "Feedback for IoT-P-1", grade: "Goed" },
        }
    }

})

export default student_project;