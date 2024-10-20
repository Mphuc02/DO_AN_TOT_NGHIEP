import Login from '../../components/admin/Login'
import {EmployeeManagement, CreateEmployee} from "../../components/admin/EmployeeManagement";
import AdminLayout from "../../layouts/body/admin/AdminLayout";
import AdminLoginLayout from "../../layouts/body/admin/AdminLoginLayout";
import DashBoard from "../../components/admin/DashBoard";
import {MedicineManagement, CreateMedicine, UpdateMedicine} from "../../components/admin/MedicineManagement";
import {CreateOrigin, OriginManagement} from "../../components/admin/OriginManagement";
import {CreateExaminationRoom, ExaminationRoomManagement} from "../../components/admin/ExaminationRoomManagement";
import {DashBoard as DoctorDashboard} from "../../components/doctor/Dashboard";
import DoctorLayout from "../../layouts/body/doctor/DoctorLayout";
import {Login as DoctorLogin} from '../../components/doctor/Login'
import {DoctorLoginLayout} from '../../layouts/body/doctor/DoctorLoginLayout'
import {WorkingScheduleManagement, CreateWorkingSchedule} from "../../components/doctor/WorkingScheduleManagement";

const publicRoutes = [
    {path: '/admin/login', component: Login, layout: AdminLoginLayout},
    {path: '/admin', component: DashBoard, layout: AdminLayout},

    //Employee
    {path: '/admin/employee-management', component: EmployeeManagement, layout: AdminLayout},
    {path: '/admin/employee-management/create', component: CreateEmployee, layout: AdminLayout},

    //Medicine
    {path: '/admin/medicine-management', component: MedicineManagement, layout: AdminLayout},
    {path: '/admin/medicine-management/create', component: CreateMedicine, layout: AdminLayout},
    {path: '/admin/medicine-management/update/:id', component: UpdateMedicine, layout: AdminLayout},
    {path: '/admin/medicine-management/origin', component: OriginManagement, layout: AdminLayout},
    {path: '/admin/medicine-management/origin/create', component: CreateOrigin, layout: AdminLayout},

    //Examination room
    {path: '/admin/examination-room-management', component: ExaminationRoomManagement, layout: AdminLayout},
    {path: '/admin/examination-room-management/create', component: CreateExaminationRoom, layout: AdminLayout},


    //Doctor
    {path: '/employee/doctor/login', component: DoctorLogin, layout: DoctorLoginLayout},
    {path: '/employee/doctor', component: DoctorDashboard, layout: DoctorLayout},

    //Doctor working schedule management
    {path: '/employee/doctor/working-schedule-management', component: WorkingScheduleManagement, layout: DoctorLayout},
    {path: '/employee/doctor/working-schedule-management/create', component: CreateWorkingSchedule, layout: DoctorLayout },
]

export {publicRoutes}