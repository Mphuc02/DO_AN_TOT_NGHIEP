import {Link, useNavigate} from 'react-router-dom';
import RoutesConstant from "../../../RoutesConstant";
import {useEffect, useState} from "react";
import {SendApiService} from "../../../service/SendApiService";
import {AUTHENTICATION} from "../../../ApiConstant";

function SideBar(){
    const [doctor, setDoctor] = useState({})
    const navigate = useNavigate();

    const onClickLogout = () => {
        const logoutRequest = {
            accessToken: localStorage.getItem('access-token'),
            refreshToken: localStorage.getItem('refresh-token')
        }

        SendApiService.postRequest(AUTHENTICATION.logout(), logoutRequest, {}, response => {
            localStorage.removeItem('access-token')
            localStorage.removeItem('refresh-token')
            localStorage.removeItem('doctor')
            navigate(RoutesConstant.DOCTOR.LOGIN)
        }, error => {

        })
    }

    useEffect(() => {
        setDoctor(JSON.parse(localStorage.getItem('doctor')))
    }, []);

    return (
        <ul className="space-y-2 font-medium">
            {doctor && (
                <>
                    {/* Quản lý lịch làm việc */}
                    <li>
                        <Link
                            to={RoutesConstant.DOCTOR.WORKING_SCHEDULE_MANAGEMENT}
                            className="flex items-center p-2 text-green-900 bg-green-50 rounded-lg hover:bg-green-100 group"
                        >
                            <svg
                                className="w-5 h-5 text-green-600 group-hover:text-green-800"
                                xmlns="http://www.w3.org/2000/svg"
                                fill="none"
                                viewBox="0 0 24 24"
                                stroke="currentColor"
                                strokeWidth="2"
                            >
                                <path
                                    strokeLinecap="round"
                                    strokeLinejoin="round"
                                    d="M8 7V3m8 4V3m-9 4h10M4 21h16M9 14h6M9 18h6M7 21V10m10 11V10"
                                />
                            </svg>
                            <span className="ml-3">Quản lý lịch làm việc</span>
                        </Link>
                    </li>

                    {/* Quản lý khám bệnh */}
                    <li>
                        <Link
                            to={RoutesConstant.DOCTOR.EXAMINATION_MANAGEMENT}
                            className="flex items-center p-2 text-green-900 bg-green-50 rounded-lg hover:bg-green-100 group"
                        >
                            <svg
                                className="w-5 h-5 text-green-600 group-hover:text-green-800"
                                xmlns="http://www.w3.org/2000/svg"
                                fill="none"
                                viewBox="0 0 24 24"
                                stroke="currentColor"
                                strokeWidth="2"
                            >
                                <path
                                    strokeLinecap="round"
                                    strokeLinejoin="round"
                                    d="M20 21V8a2 2 0 00-2-2H6a2 2 0 00-2 2v13M10 14h4M10 10h4"
                                />
                            </svg>
                            <span className="ml-3">Quản lý khám bệnh</span>
                        </Link>
                    </li>

                    {/* Tin nhắn */}
                    <li>
                        <Link
                            to={RoutesConstant.DOCTOR.CHAT}
                            className="flex items-center p-2 text-green-900 bg-green-50 rounded-lg hover:bg-green-100 group"
                        >
                            <svg
                                className="w-5 h-5 text-green-600 group-hover:text-green-800"
                                xmlns="http://www.w3.org/2000/svg"
                                fill="none"
                                viewBox="0 0 24 24"
                                stroke="currentColor"
                                strokeWidth="2"
                            >
                                <path
                                    strokeLinecap="round"
                                    strokeLinejoin="round"
                                    d="M8 10h8m-8 4h5m-5 4h6m6-8h.01M6.5 3h11l1 6H5.5L6.5 3z"
                                />
                            </svg>
                            <span className="ml-3">Tin nhắn</span>
                        </Link>
                    </li>

                    {/* Đăng xuất */}
                    <li>
                        <button
                            onClick={() => onClickLogout()}
                            className="w-full flex items-center p-2 text-red-900 bg-red-50 rounded-lg hover:bg-red-100 group">
                            <svg
                                className="w-5 h-5 text-red-600 group-hover:text-red-800"
                                xmlns="http://www.w3.org/2000/svg"
                                fill="none"
                                viewBox="0 0 24 24"
                                stroke="currentColor"
                                strokeWidth="2">
                                <path
                                    strokeLinecap="round"
                                    strokeLinejoin="round"
                                    d="M15 12H3m0 0l4-4m-4 4l4 4m13-4h-6"
                                />
                            </svg>
                            <span className="ml-3">Đăng xuất</span>
                        </button>
                    </li>
                </>
            )}
        </ul>
    )
}

export default SideBar