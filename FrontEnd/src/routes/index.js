import Login from '../components/admin/Login'
import {EmployeeManagement, CreateEmployee} from "../components/admin/EmployeeManagement";
import AdminLayout from "../layouts/body/admin/AdminLayout";
import AdminLoginLayout from "../layouts/body/admin/AdminLoginLayout";
import DashBoard from "../components/admin/DashBoard";
import {MedicineManagement, CreateMedicine, UpdateMedicine} from "../components/admin/MedicineManagement";
import {CreateOrigin, OriginManagement} from "../components/admin/OriginManagement";
import {CreateExaminationRoom, ExaminationRoomManagement} from "../components/admin/ExaminationRoomManagement";
import {DashBoard as DoctorDashboard} from "../components/doctor/Dashboard";
import DoctorLayout from "../layouts/body/doctor/DoctorLayout";
import {Login as DoctorLogin} from '../components/doctor/Login'
import {DoctorLoginLayout} from '../layouts/body/doctor/DoctorLoginLayout'
import {WorkingScheduleManagement} from "../components/doctor/WorkingScheduleManagement";
import RoutesConstant from "../RoutesConstant";
import PatientLayout from "../layouts/body/patient/PatientLayout";
import {DashBoard as PatientDashBoard} from '../components/patient/DashBoard'
import {Login as PatientLogin} from '../components/patient/Login'
import {PatientLoginLayout} from '../layouts/body/patient/PatientLoginLayout'
import {Diagnostics} from "../components/patient/Diagnostics";
import ReceiptPatient from "../components/receipt/ReceiptPatient";
import ReceiptLayout from "../layouts/body/receipt/ReceiptLayout";
import {Login as ReceiptLogin} from '../components/receipt/Login'
import {ReceiptLoginLayout} from "../layouts/body/receipt/ReceiptLoginLayout";
import PrintTicket from "../components/receipt/PrintTicket";
import {ExaminationManagement} from "../components/doctor/examination/ExaminationManagement";
import {ExaminatingPatient} from "../components/doctor/examination/ExaminatingPatient";
import {Message} from "../components/patient/Message";
import {DoctorMessage} from "../components/doctor/DoctorMessage";
import {PayForInvoice, Payment} from "../components/receipt/Payment";
import {AppointmentManagement} from "../components/patient/AppointmentManagement";
import {ExaminationResultHistories} from "../components/patient/ExaminationResultHistories";
import {CreateDisease, DiseaseManagement} from "../components/admin/DiseaseManagement";
import {CreateCost, ExaminationCostManagement} from "../components/admin/ExaminationCostManagement";
import {SignUp} from "../components/patient/SignUp";

const publicRoutes = [
    {path: RoutesConstant.ADMIN.LOGIN, component: Login, layout: AdminLoginLayout},
    {path: RoutesConstant.ADMIN.DASHBOARD, component: DashBoard, layout: AdminLayout},
    {path: RoutesConstant.ADMIN.DISEASE_MANAGEMENT, component: DiseaseManagement, layout: AdminLayout},
    {path: RoutesConstant.ADMIN.DISEASE_MANAGEMENT_CREATE, component: CreateDisease, layout: AdminLayout},
    {path: RoutesConstant.ADMIN.EXAMINATION_COST_MANAGEMENT, component: ExaminationCostManagement, layout: AdminLayout},
    {path: RoutesConstant.ADMIN.EXAMINATION_COST_MANAGEMENT_CREATE, component: CreateCost, layout: AdminLayout},

    //Employee management
    {path: RoutesConstant.ADMIN.EMPLOYEE_MANAGEMENT, component: EmployeeManagement, layout: AdminLayout},
    {path: RoutesConstant.ADMIN.EMPLOYEE_MANAGEMENT_CREATE, component: CreateEmployee, layout: AdminLayout},

    //Medicine
    {path: RoutesConstant.ADMIN.MEDICINE_MANAGEMENT, component: MedicineManagement, layout: AdminLayout},
    {path: RoutesConstant.ADMIN.MEDICINE_MANAGEMENT_CREATE, component: CreateMedicine, layout: AdminLayout},
    {path: RoutesConstant.ADMIN.MEDICINE_MANAGEMENT_UPDATE, component: UpdateMedicine, layout: AdminLayout},
    {path: RoutesConstant.ADMIN.MEDICINE_MANAGEMENT_ORIGIN, component: OriginManagement, layout: AdminLayout},
    {path: RoutesConstant.ADMIN.MEDICINE_MANAGEMENT_ORIGIN_CREATE, component: CreateOrigin, layout: AdminLayout},

    //Examination room
    {path: RoutesConstant.ADMIN.ROOM_MANAGEMENT, component: ExaminationRoomManagement, layout: AdminLayout},
    {path: RoutesConstant.ADMIN.ROOM_MANAGEMENT_CREATE, component: CreateExaminationRoom, layout: AdminLayout},

    //Doctor
    {path: RoutesConstant.DOCTOR.LOGIN, component: DoctorLogin, layout: DoctorLoginLayout},
    {path: RoutesConstant.DOCTOR.DASHBOARD, component: DoctorDashboard, layout: DoctorLayout},
    {path: RoutesConstant.DOCTOR.EXAMINATION_MANAGEMENT, component: ExaminationManagement, layout: DoctorLayout},
    {path: RoutesConstant.DOCTOR.EXAMINING_PATIENT, component: ExaminatingPatient, layout: DoctorLayout},
    {path: RoutesConstant.DOCTOR.CHAT, component: DoctorMessage, layout: DoctorLayout},

    //Doctor working schedule management
    {path: RoutesConstant.DOCTOR.WORKING_SCHEDULE_MANAGEMENT, component: WorkingScheduleManagement, layout: DoctorLayout},

    //Patient
    {path: RoutesConstant.PATIENT.DASHBOARD, component: PatientDashBoard, layout: PatientLayout},
    {path: RoutesConstant.PATIENT.LOGIN, component: PatientLogin, layout: PatientLoginLayout},
    {path: RoutesConstant.PATIENT.DIAGNOSTICS, component: Diagnostics, layout: PatientLayout},
    {path: RoutesConstant.PATIENT.CHAT, component: Message, layout: PatientLayout},
    {path: RoutesConstant.PATIENT.APPOINTMENTS_MANAGEMENT, component: AppointmentManagement, layout: PatientLayout},
    {path: RoutesConstant.PATIENT.EXAMINATION_HISTORIES, component: ExaminationResultHistories, layout: PatientLayout},
    {path: RoutesConstant.PATIENT.SIGN_UP, component: SignUp, layout: PatientLoginLayout},

    //Receipt staff
    {path: RoutesConstant.RECEIPT.LOGIN, component: ReceiptLogin, layout: ReceiptLoginLayout},
    {path: RoutesConstant.RECEIPT.RECEIPT_PATIENT, component: ReceiptPatient, layout: ReceiptLayout},
    {path: RoutesConstant.RECEIPT.PRINT_NUMBER_TICKET, component: PrintTicket, layout: ReceiptLayout},
    {path: RoutesConstant.RECEIPT.PAYMENT, component: Payment, layout: ReceiptLayout},
    {path: RoutesConstant.RECEIPT.PAY_FOR_INVOICE, component: PayForInvoice, layout: ReceiptLayout},
]

export {publicRoutes}