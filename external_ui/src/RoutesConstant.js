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
    }

    static DOCTOR  = class {
        static DASHBOARD = '/employee/doctor'
        static LOGIN = '/employee/doctor/login'
        static WORKING_SCHEDULE_MANAGEMENT = '/employee/doctor/working-schedule-management'
        static WORKING_SCHEDULE_MANAGEMENT_CREATE = '/employee/doctor/working-schedule-management/create'
    }

    static PATIENT = class {
        static DASHBOARD = '/patient'
        static LOGIN = '/patient/login'
        static DIAGNOSTICS = '/patient/diagnostics'
    }

    static RECEIPT = class {
        static LOGIN = '/employee/receipt/login'
        static RECEIPT_PATIENT = '/employee/receipt'
        static PAYMENT = "/employee/receipt/payment"
    }
}

export default RoutesConstant