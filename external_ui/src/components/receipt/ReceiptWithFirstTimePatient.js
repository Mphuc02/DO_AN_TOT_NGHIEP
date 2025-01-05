import {useEffect, useRef, useState} from "react";
import {SendApiService} from "../../service/SendApiService";
import {Greeting, HOSPITAL_INFORMATION, WEBSOCKET} from "../../ApiConstant";
import WebSocketService from "../../service/WebSocketService";
import {JwtService} from "../../service/JwtService";
import ToastPopup from "../common/ToastPopup";
import {jsPDF} from "jspdf";

const printGreetingForm = (form) => {
    const ticketData = {
        hospitalName: "Benh vien da lieu Minh Phuc",
        ...form,
    };

    const createAndPrintPDF = () => {
        const doc = new jsPDF();

        // Nội dung PDF
        doc.setFontSize(20);
        doc.text(ticketData.hospitalName, 105, 20, { align: 'center' });

        doc.setFontSize(16);
        doc.text('PHIEU SO THU TU DOI KHAM', 105, 40, { align: 'center' });

        doc.setFontSize(30);
        doc.text(`So Thu Tu: ${ticketData.examinedNumber}`, 105, 70, { align: 'center' });

        doc.setFontSize(14);
        doc.text(`Ten Benh Nhan: ${ticketData.fullName.firstName + " " + ticketData.fullName.middleName + " " + ticketData.fullName.lastName}`, 20, 100);
        doc.text(`Phong Kham: ${ticketData.roomName}`, 20, 120);

        doc.setFontSize(12);
        doc.text('Xin vui long doi den khi so cua ban duoc goi de vao kham.', 105, 140, { align: 'center' });

        // Mở hộp thoại in
        doc.autoPrint();
        window.open(doc.output('bloburl'), '_blank');
    };

    createAndPrintPDF()
}

const ReceiptWithFirstTimePatient = ({workingScheduleMap, workingRoomsMap}) => {
    const [provinces, setProvinces] = useState(new Map())
    const [districts, setDistricts] = useState(new Map())
    const [communes, setCommunes] = useState(new Map())
    const [fullName, setFullName] = useState({})
    const [numberPhone, setNumberPhone] = useState(null)
    const [address, setAddress] = useState({})
    const [symptom, setSymptom] = useState(null)
    const [workingScheduleId, setWorkingScheduleId] = useState(null)
    const [errorValidate, setErrorValidate] = useState({})
    const [webSocket, setWebSocket] = useState(new WebSocketService())
    const [createdExaminationForm, setCreatedExaminationForm] = useState(null)
    const [toast, setToast] = useState(null)
    const currentFullName = useRef(null)
    const currentRoom = useRef(null)
    const [gender, setGender] = useState(null)
    const [dateOfBirth, setDateOfBirth] = useState(null)

    const tomorrow = new Date()
    const formattedTomorrow = tomorrow.toISOString().split('T')[0];

    const hideToast = () => {
        setToast(null)
    }

    const getAllProvince = () => {
        SendApiService.getRequest(HOSPITAL_INFORMATION.ADDRESS.getUrl(), {}, response => {
            const tempMap = new Map()
            for(const province of response.data){
                tempMap.set(province.id, province)
            }
            console.log(tempMap)
            setProvinces(tempMap)
        }, error => {

        })
    }

    const receivedCreatedExaminationForm = (message) => {
        const data = JSON.parse(message)
        setToast({message: data.message, type: 'ok'})
        console.log(data.data, currentRoom.current)
        setCreatedExaminationForm({...data.data, fullName: currentFullName.current, roomName: currentRoom.current})
    }

    const connectToWebsocket = () => {
        const topics = [WEBSOCKET.updatedNumberExaminationForm(JwtService.geUserFromToken())]
        webSocket.connectAndSubscribe(topics, (message) => {
            receivedCreatedExaminationForm(message)
        })
    }

    const disconnect = () => {
        webSocket.disconnect()
    }

    useEffect(() => {
        getAllProvince()
        connectToWebsocket()

        return () => {
            disconnect()
        }
    }, []);

    const onChangeProvince = (provinceId) => {
        setAddress({...address, provinceId: provinceId})
        SendApiService.getRequest(HOSPITAL_INFORMATION.ADDRESS.getByProvinceId(provinceId), {}, response => {
            const tempMap = new Map()
            for(const district of response.data){
                tempMap.set(district.id, district)
            }
            setDistricts(tempMap)
            setCommunes(new Map())
        }, error => {

        })
    }

    const onChangeDistrict = (districtId) => {
        setAddress({...address, districtId: districtId})
        SendApiService.getRequest(HOSPITAL_INFORMATION.ADDRESS.getByDistrictId(districtId), {}, response => {
            const tempMap = new Map()
            for(const commune of response.data){
                tempMap.set(commune.id, commune)
            }
            setCommunes(tempMap)
        }, error => {

        })
    }

    const onClickSave = () => {
        currentFullName.current = fullName

        const examinationForm = {
            patient: {
                fullName: fullName,
                numberPhone: numberPhone,
                address: address,
                gender: gender,
                dateOfBirth: dateOfBirth
            },
            symptom: symptom,
            workingScheduleId: workingScheduleId
        }

        SendApiService.postRequest(Greeting.ExaminationForm.firstTime(), examinationForm, {}, response => {
            setErrorValidate({})
        }, error => {
            if(error.status === 400){
                setErrorValidate(error.response.data.fields)
            }
        })
    }

    return (
        <>
            <table className="w-full border-collapse border border-gray-300 text-sm">
                <thead><tr></tr></thead>
                <tbody>
                <tr>
                    <td colSpan="2"
                        className="bg-blue-100 text-left text-lg font-semibold p-3 border-b border-gray-300"><h3>Thông
                        tin liên hệ</h3></td>
                </tr>

                <tr>
                    <td></td>
                    <td className="text-red-500 text-sm font-medium">{errorValidate['patient.fullName.lastName']}</td>
                </tr>

                <tr>
                    <td className="pl-2 pr-1 font-medium text-gray-700 text-left w-32 mb-4">Họ:</td>
                    <td className="pl-1"><input className="w-1/2 border-2 border-gray-800 rounded-md p-2 mb-2 mt-2"
                                                onChange={(e) => setFullName({...fullName, lastName: e.target.value})}/>
                    </td>
                </tr>

                <tr>
                    <td></td>
                    <td className="text-red-500 text-sm font-medium">{errorValidate['patient.fullName.middleName']}</td>
                </tr>

                <tr>
                    <td className="pl-2 pr-1 font-medium text-gray-700 text-left w-32 mb-4">Tên đệm:</td>
                    <td className="pl-1"><input className="w-1/2 border-2 border-gray-800 rounded-md p-2 mb-2 mt-2"
                                                onChange={(e) => setFullName({
                                                    ...fullName,
                                                    middleName: e.target.value
                                                })}/></td>
                </tr>

                <tr>
                    <td></td>
                    <td className="text-red-500 text-sm font-medium">{errorValidate['patient.fullName.firstName']}</td>
                </tr>

                <tr>
                    <td className="pl-2 pr-1 font-medium text-gray-700 text-left w-32 mb-4">Tên bệnh nhân:</td>
                    <td><input className="w-1/2 border-2 border-gray-800 rounded-md p-2 mb-2 mt-2"
                               onChange={(e) => setFullName({...fullName, firstName: e.target.value})}/></td>
                </tr>

                <tr>
                    <td></td>
                    <td className="text-red-500 text-sm font-medium">{errorValidate['patient.gender']}</td>
                </tr>

                <tr>
                    <td className="pl-2 pr-1 font-medium text-gray-700 text-left w-32 mb-4">Giới tính:</td>
                    <td>
                        <select className="border-2 border-gray-800 rounded-md p-2 mb-2 mt-2"
                                onChange={(e) => setGender(e.target.value)}>
                            <option>----------</option>
                            <option value={1}>Nam</option>
                            <option value={2}>Nữ</option>
                        </select>
                    </td>
                </tr>

                <tr>
                    <td></td>
                    <td className="text-red-500 text-sm font-medium">{errorValidate['patient.dateOfBirth']}</td>
                </tr>

                <tr>
                    <td className="pl-2 pr-1 font-medium text-gray-700 text-left w-32 mb-4">Ngày sinh:</td>
                    <td><input type={"date"}
                               max={formattedTomorrow}
                               onChange={e => setDateOfBirth(e.target.value)}/></td>
                </tr>

                <tr>
                    <td></td>
                    <td className="text-red-500 text-sm font-medium">{errorValidate['patient.numberPhone']} {errorValidate['patient']}</td>
                </tr>

                <tr>
                    <td className="pl-2 pr-1 font-medium text-gray-700 text-left w-32 mb-4">Số điện thoại:</td>
                    <td><input className="w-1/2 border-2 border-gray-800 rounded-md p-2 mb-2 mt-2"
                               onChange={(e) => setNumberPhone(e.target.value)}/></td>
                </tr>

                <tr>
                    <td colSpan="2"
                        className="bg-blue-100 text-left text-lg font-semibold p-3 border-b border-gray-300">Địa chỉ
                    </td>
                    <td></td>
                </tr>

                <tr>
                    <td></td>
                    <td className="text-red-500 text-sm font-medium">{errorValidate['patient.address.provinceId']}</td>
                </tr>

                <tr>
                    <td className="pl-2 pr-1 font-medium text-gray-700 text-left w-32 mb-4">Tỉnh/Thành phố:</td>
                    <td>
                        <select className="border-2 border-gray-800 rounded-md p-2 mb-2 mt-2"
                                onChange={(e) => onChangeProvince(e.target.value)}>
                            <option>----------</option>
                            {[...provinces].map(([key, value]) => {
                                return <option key={key} value={key}>{value.name}</option>
                            })}
                        </select>
                    </td>
                </tr>

                <tr>
                    <td></td>
                    <td className="text-red-500 text-sm font-medium">{errorValidate['patient.address.districtId']}</td>
                </tr>

                <tr>
                    <td className="pl-2 pr-1 font-medium text-gray-700 text-left w-32 mb-4">Quận/Huyện:</td>
                    <td>
                        <select className="border-2 border-gray-800 rounded-md p-2 mb-2 mt-2"
                                onChange={(e) => onChangeDistrict(e.target.value)}>
                            <option>----------</option>
                            {[...districts].map(([key, value]) => {
                                return <option key={key} value={key}>{value.name}</option>
                            })}
                        </select>
                    </td>
                </tr>

                <tr>
                    <td></td>
                    <td className="text-red-500 text-sm font-medium">{errorValidate['patient.address.communeId']}</td>
                </tr>

                <tr>
                    <td className="pl-2 pr-1 font-medium text-gray-700 text-left w-32 mb-4">Xã/Phường:</td>
                    <td><select className="border-2 border-gray-800 rounded-md p-2 mb-2 mt-2"
                                onChange={(e) => setAddress({...address, communeId: e.target.value})}>
                        <option>----------</option>
                        {[...communes].map(([key, value]) => {
                            return <option key={key} value={key}>{value.name}</option>
                        })}
                    </select></td>
                </tr>

                <tr>
                    <td></td>
                    <td className="text-red-500 text-sm font-medium">{errorValidate['patient.address.street']}</td>
                </tr>

                <tr>
                    <td className="pl-2 pr-1 font-medium text-gray-700 text-left w-32 mb-4">Số nhà:</td>
                    <td><input className="w-1/2 border-2 border-gray-800 rounded-md p-2 mb-2 mt-2"
                               onChange={e => setAddress({...address, street: e.target.value})}/></td>
                </tr>

                <tr>
                    <td colSpan="2"
                        className="bg-blue-100 text-left text-lg font-semibold p-3 border-b border-gray-300"><h3>Thông
                        tin khám bệnh</h3></td>
                    <td></td>
                </tr>

                <tr>
                    <td></td>
                    <td className="text-red-500 text-sm font-medium">{errorValidate['workingScheduleId']}</td>
                </tr>

                <tr>
                    <td className="pl-2 pr-1 font-medium text-gray-700 text-left w-32 mb-4">Chọn phòng khám:</td>
                    <td>
                        <select className="border-2 border-gray-800 rounded-md p-2 mb-2 mt-2" onChange={e => {
                            currentRoom.current = e.target.options[e.target.selectedIndex].text;
                            setWorkingScheduleId(e.target.value)
                        }}>
                            <option>----------</option>
                            {[...workingScheduleMap].map(([key, value]) => {
                                const fullName = value.doctor.fullName
                                const fullNameStr = fullName.lastName + ' ' + fullName.middleName + ' ' + fullName.firstName
                                return <option key={key}
                                               value={value.id}>Phòng {workingRoomsMap.get(value.roomId).name} - bác
                                    sĩ {fullNameStr}</option>
                            })}
                        </select>
                    </td>
                </tr>

                <tr>
                    <td></td>
                    <td className="text-red-500 text-sm font-medium">{errorValidate['symptom']}</td>
                </tr>

                <tr>
                    <td className="pl-2 pr-1 font-medium text-gray-700 text-left w-32 mb-4">Triệu chứng</td>
                    <td><input className="w-1/2 border-2 border-gray-800 rounded-md p-2 mb-2 mt-2"
                               onChange={e => setSymptom(e.target.value)}/></td>
                </tr>
                </tbody>
            </table>

            {!createdExaminationForm &&
                <button className="bg-green-500 text-white px-4 py-2 rounded-md hover:bg-green-600 mt-10" onClick={() => onClickSave()}>Lưu thông tin</button>}


            {toast && <ToastPopup message={toast.message} type={toast.type} onClose={hideToast}/>}

            {createdExaminationForm &&
                <button className="bg-green-500 text-white px-4 py-2 rounded-md hover:bg-green-600 mt-10"
                        onClick={() => printGreetingForm(createdExaminationForm)}>In phiếu khám bệnh</button>}
        </>
    )
}

export {ReceiptWithFirstTimePatient}