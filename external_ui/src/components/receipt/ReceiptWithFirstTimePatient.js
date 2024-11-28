import {useEffect, useState} from "react";
import {SendApiService} from "../../service/SendApiService";
import {HOSPITAL_INFORMATION} from "../../ApiConstant";

const ReceiptWithFirstTimePatient = () => {
    const [provinces, setProvinces] = useState(new Map())
    const [districts, setDistricts] = useState(new Map())
    const [communes, setCommunes] = useState(new Map())

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

    useEffect(() => {
        getAllProvince()
    }, []);

    const onChangeProvince = (provinceId) => {
        SendApiService.getRequest(HOSPITAL_INFORMATION.ADDRESS.getByProvinceId(provinceId), {}, response => {
            const tempMap = new Map()
            for(const district of response.data){
                tempMap.set(district.id, district)
            }
            setDistricts(tempMap)
        }, error => {

        })
    }

    const onChangeDistrict = (districtId) => {
        SendApiService.getRequest(HOSPITAL_INFORMATION.ADDRESS.getByDistrictId(districtId), {}, response => {
            const tempMap = new Map()
            for(const commune of response.data){
                tempMap.set(commune.id, commune)
            }
            setCommunes(tempMap)
        }, error => {

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
                        <td>Họ:</td>
                        <td> <input /></td>
                    </tr>
                    <tr>
                        <td>Tên đệm:</td>
                        <td><input/> </td>
                    </tr>
                    <tr>
                        <td>Tên bệnh nhân:</td>
                        <td><input/></td>
                    </tr>
                    <tr>
                        <td>Số điện thoại:</td>
                        <td><input/></td>
                    </tr>

                    <tr>
                        <td>Địa chỉ</td>
                        <td></td>
                    </tr>

                    <tr>
                        <td>Tỉnh/Thành phố:</td>
                        <td>
                            <select onChange={(e) => onChangeProvince(e.target.value)}>
                                {[...provinces].map(([key, value]) => {
                                    return <option key={key} value={key}>{value.name}</option>
                                })}
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td>Quận/Huyện:</td>
                        <td>
                            <select onChange={(e) => onChangeDistrict(e.target.value)}>
                                {[...districts].map(([key, value]) => {
                                    return <option key={key} value={key}>{value.name}</option>
                                })}
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td>Xã/Phường:</td>
                        <td><select>
                            {[...communes].map(([key, value]) => {
                                return <option key={key} value={key}>{value.name}</option>
                            })}
                        </select></td>
                    </tr>

                    <tr>
                        <td><h3>Thông tin khám bệnh</h3></td>
                        <td></td>
                    </tr>

                    <tr>
                        <td>Chọn phòng khám:</td>
                        <td><input/></td>
                    </tr>

                    <tr>
                        <td>Triệu chứng</td>
                        <td><input /></td>
                    </tr>
                </tbody>
            </table>
        </>
    )
}

export {ReceiptWithFirstTimePatient}