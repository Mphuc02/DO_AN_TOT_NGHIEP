import {useEffect, useState} from "react";
import {SendApiService} from "../../service/SendApiService";
import {Greeting, HOSPITAL_INFORMATION, WEBSOCKET} from "../../ApiConstant";
import WebSocketService from "../../service/WebSocketService";
import {JwtService} from "../../service/JwtService";

const ReceiptWithFirstTimePatient = ({workingScheduleMap, workingRoomsMap}) => {
    const [provinces, setProvinces] = useState(new Map())
    const [districts, setDistricts] = useState(new Map())
    const [communes, setCommunes] = useState(new Map())
    const [fullName, setFullName] = useState({})
    const [numberPhone, setNumberPhone] = useState(null)
    const [address, setAddress] = useState({})
    const [symptom, setSymptom] = useState(null)
    const [workingSchedule, setWorkingSchedule] = useState(null)
    // const [errorValidate, setErrorValidate] = useState({patient: {fullName: {}, address: {}}})
    const [errorValidate, setErrorValidate] = useState({})
    const [webSocket, setWebSocket] = useState(new WebSocketService())
    const [createdExaminationForm, setCreatedExaminationForm] = useState(null)

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
        alert(data.message)
        console.log(data.data)
        setCreatedExaminationForm(data.data)
    }

    const connectToWebsocket = () => {
        const topics = [WEBSOCKET.updatedNumberExaminationForm(JwtService.geUserFromToken())]
        webSocket.connectAndSubscribe(topics, (message) => {
            receivedCreatedExaminationForm (message)
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
        const examinationForm = {
            patient: {
                fullName: fullName,
                numberPhone: numberPhone,
                address: address
            },
            symptom: symptom,
            workingSchedule: workingSchedule
        }

        console.log(examinationForm)
        SendApiService.postRequest(Greeting.ExaminationForm.firstTime(), examinationForm, {}, response => {

        }, error => {
            if(error.status === 400){
                setErrorValidate(error.response.data.fields)
                console.log(error.response.data.fields)
            }
        })
    }

    return (
        <>
            <table>
                <thead><tr></tr></thead>
                <tbody>
                <tr>
                    <td><h3>Thông tin liên hệ</h3></td>
                </tr>

                <tr>
                    <td></td>
                    <td className="text-red-500 text-sm font-medium">{errorValidate['patient.fullName.lastName']}</td>
                </tr>

                <tr>
                    <td>Họ:</td>
                    <td><input onChange={(e) => setFullName({...fullName, lastName: e.target.value})}/></td>
                </tr>

                <tr>
                    <td></td>
                    <td className="text-red-500 text-sm font-medium">{errorValidate['patient.fullName.middleName']}</td>
                </tr>

                <tr>
                    <td>Tên đệm:</td>
                    <td><input onChange={(e) => setFullName({...fullName, middleName: e.target.value})}/></td>
                </tr>

                <tr>
                    <td></td>
                    <td className="text-red-500 text-sm font-medium">{errorValidate['patient.fullName.firstName']}</td>
                </tr>

                <tr>
                    <td>Tên bệnh nhân:</td>
                    <td><input onChange={(e) => setFullName({...fullName, firstName: e.target.value})}/></td>
                </tr>

                <tr>
                    <td></td>
                    <td className="text-red-500 text-sm font-medium">{errorValidate['patient.numberPhone']} {errorValidate['patient']}</td>
                </tr>

                <tr>
                    <td>Số điện thoại:</td>
                    <td><input onChange={(e) => setNumberPhone(e.target.value)}/></td>
                </tr>

                <tr>
                    <td>Địa chỉ</td>
                    <td></td>
                </tr>

                <tr>
                    <td></td>
                    <td className="text-red-500 text-sm font-medium">{errorValidate['patient.address.provinceId']}</td>
                </tr>

                <tr>
                    <td>Tỉnh/Thành phố:</td>
                    <td>
                        <select onChange={(e) => onChangeProvince(e.target.value)}>
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
                    <td>Quận/Huyện:</td>
                    <td>
                        <select onChange={(e) => onChangeDistrict(e.target.value)}>
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
                    <td>Xã/Phường:</td>
                    <td><select onChange={(e) => setAddress({...address, communeId: e.target.value})}>
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
                    <td>Số nhà:</td>
                    <td><textarea onChange={e => setAddress({...address, street: e.target.value})}/></td>
                </tr>

                <tr>
                    <td><h3>Thông tin khám bệnh</h3></td>
                    <td></td>
                </tr>

                <tr>
                    <td></td>
                    <td className="text-red-500 text-sm font-medium">{errorValidate['workingSchedule']}</td>
                </tr>

                <tr>
                    <td>Chọn phòng khám:</td>
                    <td>
                        <select onChange={e => setWorkingSchedule(e.target.value)}>
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
                    <td>Triệu chứng</td>
                    <td><textarea onChange={e => setSymptom(e.target.value)}/></td>
                </tr>
                </tbody>
            </table>

            <button onClick={() => onClickSave()}>Lưu thông tin</button>
        </>
    )
}

export {ReceiptWithFirstTimePatient}