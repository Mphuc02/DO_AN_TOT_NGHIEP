class RoutesConstant{
    static ADMIN = class {
        static LOGIN = '/admin/login'
        static DASHBOARD = '/admin'
        static EMPLOYEE_MANAGEMENT = '/admin/employee-management'
        static EMPLOYEE_MANAGEMENT_CREATE = '/admin/employee-management/create'
        static MEDICINE_MANAGEMENT = '/admin/medicine-management'
        static MEDICINE_MANAGEMENT_CREATE = '/admin/medicine-management/create'
        static MEDICINE_MANAGEMENT_UPDATE = '/admin/medicine-management/update/:id'
        static MEDICINE_MANAGEMENT_ORIGIN = '/admin/medicine-management/origin'
        static MEDICINE_MANAGEMENT_ORIGIN_CREATE = '/admin/medicine-management/origin/create'
        static ROOM_MANAGEMENT = '/admin/examination-room-management'
        static ROOM_MANAGEMENT_CREATE = '/admin/examination-room-management/create'
        static DISEASE_MANAGEMENT = "/admin/disease-management"
        static DISEASE_MANAGEMENT_CREATE = "/admin/disease-management/create"
        static EXAMINATION_COST_MANAGEMENT = "/admin/examination-costs"
        static EXAMINATION_COST_MANAGEMENT_CREATE = "/admin/examination-costs/create"

        static UPDATE_MEDICINE = (id) => {
            return this.MEDICINE_MANAGEMENT + "/update/" + id
        }

        static UPDATE_ORIGIN_MEDICINE = (id) => {
            return this.MEDICINE_MANAGEMENT_ORIGIN + '/update/' + id
        }

        static UPDATE_EMPLOYEE = (id) => {
            return this.EMPLOYEE_MANAGEMENT + "/update/" + id
        }

        static UPDATE_ROOM = (id) => {
            return this.ROOM_MANAGEMENT + "/update/" + id
        }

    }

    static DOCTOR  = class {
        static DASHBOARD = '/employee/doctor'
        static LOGIN = '/employee/doctor/login'
        static WORKING_SCHEDULE_MANAGEMENT = '/employee/doctor/working-schedule-management'
        static WORKING_SCHEDULE_MANAGEMENT_CREATE = '/employee/doctor/working-schedule-management/create'
        static EXAMINATION_MANAGEMENT = '/employee/doctor/examination-management'
        static EXAMINING_PATIENT = '/employee/doctor/examining-patient/:id'
        static EXAMINING_PATIENT_WITH_ID = (id) => {
            return `/employee/doctor/examining-patient/${id}`
        }
        static CHAT = "/employee/doctor/chat"
    }

    static PATIENT = class {
        static DASHBOARD = '/patient'
        static LOGIN = '/patient/login'
        static APPOINTMENTS_MANAGEMENT = "/patient/appointments-management"
        static DIAGNOSTICS = '/patient/diagnostics'
        static CHAT = "/patient/messages"
        static EXAMINATION_HISTORIES = "/patient/examination-histories"
    }

    static RECEIPT = class {
        static LOGIN = '/employee/receipt/login'
        static RECEIPT_PATIENT = '/employee/receipt'
        static PAYMENT = "/employee/receipt/payment"
        static PRINT_NUMBER_TICKET = "/employee/receipt/print-number-ticket"
        static PAY_FOR_INVOICE = "/employee/receipt/payment/:id"
        static payForInvoice = (id) => {
            return `/employee/receipt/payment/${id}`
        }
    }
}

export default RoutesConstant