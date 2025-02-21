import {Link} from "react-router-dom";
import {useEffect, useState} from "react";
import logo from "../../../imgs/logo.jpg"
import RoutesConstant from "../../../RoutesConstant";

function Header({patient}) {
    const [thisPatient, setThisPatient] = useState(null)

    useEffect(() => {
        setThisPatient(patient)
    }, [patient]);

    return (
        <div className="px-3 py-3 lg:px-5 lg:pl-3 bg-blue-50 border-b border-blue-200">
            <div className="flex items-center justify-between">
                <div className="flex items-center justify-start rtl:justify-end">
                    <button
                        data-drawer-target="logo-sidebar"
                        data-drawer-toggle="logo-sidebar"
                        aria-controls="logo-sidebar"
                        type="button"
                        className="inline-flex items-center p-2 text-sm text-blue-700 rounded-lg sm:hidden hover:bg-blue-100 focus:outline-none focus:ring-2 focus:ring-blue-300">
                        <span className="sr-only">Open sidebar</span>
                        <svg
                            className="w-6 h-6"
                            aria-hidden="true"
                            fill="currentColor"
                            viewBox="0 0 20 20"
                            xmlns="http://www.w3.org/2000/svg">
                            <path
                                clipRule="evenodd"
                                fillRule="evenodd"
                                d="M2 4.75A.75.75 0 012.75 4h14.5a.75.75 0 010 1.5H2.75A.75.75 0 012 4.75zm0 10.5a.75.75 0 01.75-.75h7.5a.75.75 0 010 1.5h-7.5a.75.75 0 01-.75-.75zM2 10a.75.75 0 01.75-.75h14.5a.75.75 0 010 1.5H2.75A.75.75 0 012 10z"
                            />
                        </svg>
                    </button>
                    <a href="https://flowbite.com" className="flex ms-2 md:me-24">
                        <img
                            className="w-16 h-16 mr-2 rounded-full"
                            src={logo}
                            alt="logo"
                        />
                        <span
                            className="self-center text-xl font-semibold sm:text-2xl whitespace-nowrap text-green-700">
                                Phòng khám da liễu Minh Phúc
                            </span>
                    </a>
                </div>
                {thisPatient && (
                    <div className="flex items-center">
                        <div className="text-blue-700">
                            Xin chào,{" "}
                            {(() => {
                                if (!thisPatient) {
                                    return null;
                                }
                                return `${thisPatient.fullName.firstName} ${thisPatient.fullName.middleName} ${thisPatient.fullName.lastName}`;
                            })()}
                        </div>
                        <div className="flex items-center ms-3">
                            <div>
                                <button
                                    type="button"
                                    className="flex text-sm bg-green-600 hover:bg-green-700 text-white rounded-full focus:ring-4 focus:ring-green-300"
                                    aria-expanded="false"
                                    data-dropdown-toggle="dropdown-user">
                                    <img
                                        className="w-8 h-8 rounded-full"
                                        src={thisPatient.avatar}
                                        alt="user photo"
                                    />
                                </button>
                            </div>
                        </div>
                    </div>
                )}
                {!thisPatient && (
                    <div className="flex items-center">
                        <div className="flex items-center ms-3">
                            <Link
                                to={RoutesConstant.PATIENT.LOGIN}
                                className="text-blue-700 hover:text-blue-800 font-medium">
                                Đăng nhập
                            </Link>
                        </div>
                    </div>
                )}
            </div>
        </div>
    )
}

export default Header