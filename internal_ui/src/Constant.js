import {get} from "axios";

class HOST{
    static getHost = () => { return "http://localhost:9000"}
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
}

class EMPLOYYEE{
    static getUrl = () => {
        return HOST.getHost() + "/api/v1/employees"
    }

    static id = (id) => {
        return this.getUrl() + "/" + id;
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
    }

    static Origin = class {
        static getUrl = () => {
            return HOST.getHost() + "/api/v1/origins";
        }
    }
}

class ROLE{
    static role = new Map([
        ['ADMIN_UPDATE', "Quản trị viên"],
        ['ADMIN_READ', "Quản trị viên"],
        ['ADMIN_CREATE', "Quản trị viên"],
        ['ADMIN_DELETE', "Quản trị viên"],
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
}

class HOSPITAL_INFORMATION{
    static EXAMINATION_ROOM = class {
        static getUrl = () => {
            return HOST.getHost() + "/api/v1/examination-rooms"
        }

        static findByIds = () => {
            return this.getUrl() + "/find-by-ids"
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
}

export {HOST, AUTHENTICATION, EMPLOYYEE, ROLE, WEBSOCKET, MEDICINE, HOSPITAL_INFORMATION, WORKING_SCHEDULE}