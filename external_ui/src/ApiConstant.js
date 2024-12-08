
class HOST{
    static getHost = () => { return "http://localhost:9010"}
}

class MinioUrl{
    static getHost = () => {
        return 'http://localhost:9000'
    }

    static downloadFile = (url) => {
        return this.getHost() + `/${url}`
    }
}

class AUTHENTICATION{
    static getUrl = () => {
        return HOST.getHost() + "/api/v1/authentication";
    }

    static authenticate = () => {
        return this.getUrl() + "/authenticate";
    }

    static exchangeToken = () => {
        return this.getUrl() + "/exchange-token";
    }

    static authenticateEmployee = () => {
        return this.getUrl() + "/authentication-employee"
    }

    static createEmployee = () => {
        return this.getUrl() + "/create-employee"
    }

    static logout = () => {
        return this.getUrl() + "/logout"
    }
}

class EMPLOYYEE{
    static getUrl = (permission) => {
        if(permission === null || permission === '')
            return HOST.getHost() + '/api/v1/employees'
        return HOST.getHost() + `/api/v1/employees?role=${permission}`
    }

    static id = (id) => {
        return this.getUrl() + "/" + id;
    }

    static findByIds = () => {
        return this.getUrl('') + "/find-by-ids"
    }

    static getLoggedUserInformation = () => {
        return this.getUrl('') + "/get-logged-user-information"
    }
}

class MEDICINE{
    static Medicine = class {
        static getUrl = () => {
            return HOST.getHost() + "/api/v1/medicines";
        }

        static getById = (id) => {
            return this.getUrl() + `/${id}`;
        }

        static search = (q) => {
            return this.getUrl() + `/search?q=${q}`
        }
    }

    static Origin = class {
        static getUrl = () => {
            return HOST.getHost() + "/api/v1/origins";
        }
    }
}

class ROLE{
    static role = new Map([
        ['ADMIN', "Quản trị viên"],
        ['DOCTOR', "Bác sĩ"],
        ['RECEPTION_STAFF', "Nhân viên tiếp đón"],
        ['MEDICINE_DISPENSER', "Nhân viên kho thuốc"]
    ])

    static getRole = (role) => {
        return this.role.get(role)
    }

    static getUrl = () => {
        return HOST.getHost() + "/api/v1/roles";
    }
}

class WEBSOCKET{
    static getUrl = () => {
        return HOST.getHost() + "/hospital_system";
    }

    static topicCreateEmployee = (id) => {
        return "/topic/create-employee/" + id;
    }

    static topicCreatedEmployee = (id) => {
        return "/topic/created-employee/" + id;
    }

    static processedImage = (id) => {
        return "/topic/processed-image/" + id
    }

    static updatedNumberExaminationForm = (id) => {
        return "/topic/updated-number-examination-form/" + id
    }

    static chat = (id) => {
        return `/topic/chat/user/${id}`
    }

    static appSendImage = (id) => {
        return `/app/chat/image/doctor/${id}`
    }
}

class HOSPITAL_INFORMATION{
    static EXAMINATION_ROOM = class {
        static getUrl = () => {
            return HOST.getHost() + "/api/examination-rooms"
        }

        static findByIds = () => {
            return this.getUrl() + "/find-by-ids"
        }
    }
    static DISEASES = class {
        static getUrl = () => {
            return HOST.getHost() + "/api/diseases"
        }
    }

    static ADDRESS = class{
        static getUrl = () => {
            return HOST.getHost() + "/api/provinces"
        }

        static getByProvinceId = (id) => {
            return this.getUrl() + `/by-province/${id}`
        }

        static getByDistrictId = (id) => {
            return this.getUrl() + `/by-district/${id}`
        }

        static getDetail = () => {
            return this.getUrl() + "/get-address-detail"
        }
    }
}

class WORKING_SCHEDULE{
    static getUrl = () => {
        return HOST.getHost() + "/api/v1/working-schedules"
    }

    static getSchedulesInMonthOfEmployee = (year, month) => {
        return this.getUrl() + `/schedules-in-month?year=${year}&month=${month}`
    }

    static getUrlById = (id) => {
        return this.getUrl() + "/" + id;
    }

    static getSchedulesByDate = (date) => {
        return this.getUrl() + `/find/by-date?date=${date}`
    }
}

class AI{
    static getUrl = () => {
        return HOST.getHost() + "/api/v1/ai"
    }

    static diagnostic = () => {
        return this.getUrl() + "/diagnostic"
    }
}

class PATIENT{
    static APPOINTMENT = class {
        static getUrl = () => {
            return HOST.getHost() + "/api/appointments"
        }

        static getAppointmentsOfToday = () => {
            return this.getUrl() + "/today"
        }

        static findAppointmentDetailByAppointmentId = (id) => {
            return this.getUrl() + "/find-detail-by-appointment-id/" + id
        }

        static doctorCreateAppointment = () => {
            return this.getUrl() + "/create-appointment-by-doctor"
        }
    }

    static PATIENT_API = class {
        static getUrl = () => {
            return HOST.getHost() + "/api/patients"
        }

        static getByIds = () => {
            return this.getUrl() + "/get/by-ids"
        }

        static byId = (id) => {
            return this.getUrl() + "/" + id
        }

        static getUserInformation = () =>{
            return this.getUrl() + "/get-logged-user-information";
        }
    }
}


class Greeting{
    static ExaminationForm = class{
        static getGetUrl = () => {
            return HOST.getHost() + "/api/v1/greeting"
        }

        static withAppointment = () => {
            return this.getGetUrl() + "/with-appointment"
        }

        static findReceivedPatientsToday = () => {
            return this.getGetUrl() + "/find/received-patients-today";
        }

        static firstTime = () => {
            return this.getGetUrl() + "/first-time"
        }
    }
}

class ExaminationResult{
    static ExaminationResultUrl = class{
        static getUrl = () => {
            return HOST.getHost() + "/api/examination-results"
        }

        static findWaitingExaminationPatients = () => {
            return this.getUrl() + '/find/waiting-examination-patients'
        }

        static findById = (id) => {
            return this.getUrl() + "/" + id
        }
    }

    static MedicineConsultationForm = class {
        static getUrl = () => {
            return HOST.getHost() + "/api/consultation-form"
        }

        static byId = (id) => {
            return this.getUrl() + `/${id}`
        }
    }
}

const Chat = class {
    static Message = class {
        static getUrl = () => {
            return HOST.getHost() + "/api/chat"
        }

        static getMessageByReceiverId = (id, pageable) => {
            return this.getUrl() + `/with/${id}?page=${pageable.page}`
        }

        static sendImage = (id) => {
            return this.getUrl() + "/send-image/" + id
        }
    }

    static RelationShip = class {
        static getUrl = () => {
            return HOST.getHost() + "/api/relation-ship"
        }

        static findRelationShipsOfPatient = (page) => {
            return this.getUrl() + `/find-relation-ships-of-patient?page=${page}`
        }

        static findRelationShipOfDoctor = (page) => {
            return this.getUrl() + `/find-relation-ships-of-doctor?page=${page}`
        }
    }
}

const PaymentApi = class {
    static getUrl = () => {
        return HOST.getHost() + "/api/invoices"
    }

    static getUnPaid = () => {
        return this.getUrl() + "/unpaid"
    }

    static byId = (id) => {
        return this.getUrl() + `/${id}`
    }

    static payInCash = (id) => {
        return this.getUrl() + `/pay-in-cash/${id}`
    }
}

export {HOST, AUTHENTICATION, EMPLOYYEE, ROLE, WEBSOCKET, MEDICINE, HOSPITAL_INFORMATION, WORKING_SCHEDULE, AI, PATIENT, Greeting, ExaminationResult, Chat, MinioUrl, PaymentApi}