package com.example.Authorization.Redis;

import com.example.Authorization.DTO.InfoUserRedis;
import com.example.Authorization.Interface.GateWayClient;
import feign.FeignException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class GetInfoUserInGateway {

    private final GateWayClient gateWayClient;

    public GetInfoUserInGateway(GateWayClient gateWayClient) {
        this.gateWayClient = gateWayClient;
    }

//    public InfoUserRedis getInfoUserName(String userName) {
//        try {
//            ResponseEntity<InfoUserRedis> response = gateWayClient.getUserRole(userName);
//
//            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
//                return response.getBody();
//            } else {
//                System.out.println("⚠️ Không lấy được dữ liệu user");
//                return null;
//            }
//
//        } catch (FeignException.NotFound ex) {
//            System.out.println("❌ User not found: " + userName);
//            return null;
//        } catch (FeignException ex) {
//            System.out.println("❌ Feign error: " + ex.status() + " - " + ex.getMessage());
//            return null;
//        }
//    }


//    public ResponseEntity<String> saveInfoUserName(InfoUserRedis infoUserRedis){
//       return  gateWayClient.saveUserRole(infoUserRedis);
//    }
//    public ResponseEntity<String> updateVersionToken(String userName,String versionUpdate){
//        return  gateWayClient.updateVersionToken(userName,versionUpdate);
//    }

}
