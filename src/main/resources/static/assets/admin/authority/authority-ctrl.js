app.controller("authority-ctrl", function ($scope, $http, $location, $routeParams) {
    $scope.roles = []
    $scope.admins = []
    $scope.authorities = []

    $scope.initalize =  function (){
        //load add roles
        $http.get("/rest/roles").then(resp => {
            $scope.roles = resp.data
        })
        //load staff anf directors
        $http.get("/rest/accounts?admin=true").then(resp => {
            $scope.admins = resp.data
        })
        //load authorities of staff and director
        $http.get("/rest/authorities?admin=true").then(resp => {
            console.log("Authorities data:", resp.data); // Kiểm tra cấu trúc dữ liệu
            $scope.authorities = Array.isArray(resp.data) ? resp.data : [];
        }).catch(error => {
            $location.path("/unauthorized");
        });


    }
    $scope.authority_of = function (acc, role) {
        if (Array.isArray($scope.authorities)) {
            return $scope.authorities.find(ur =>
                ur && ur.account && ur.account.username === acc.username &&
                ur.role && ur.role.id === role.id
            );
        }
        return undefined;
    }



    $scope.authority_changed = function (acc, role){
        var authority = $scope.authority_of(acc, role)
        if (authority){
            $scope.revoke_authority(authority)

        }else {
            authority = {account: acc, role: role}
            $scope.grant_authority(authority)
        }
    }
    //thêm mới authority
    $scope.grant_authority = function (authority){
        console.log("Before push:", $scope.authorities);
        $http.post(`/rest/authorities`, authority).then(resp => {
            console.log("Response data:", resp.data);
            if (Array.isArray($scope.authorities)) {
                $scope.authorities.push(resp.data);
            } else {
                console.error("$scope.authorities is not an array");
            }
            alert("Cấp quyền sử dụng thành công");
        }).catch(error => {
            alert("Cấp quyền sử dụng thất bại");
            console.log("Error", error);
        });

    }
    //xóa authority
    $scope.revoke_authority = function (authority){
        $http.delete(`/rest/authorities/${authority.id}`).then(resp => {
            var index = $scope.authorities.findIndex(a => a.id == authority.id)
            $scope.authorities.splice(index, 1)
            alert("Thu hồi quyền sử dụng thành công")

        }).catch(error => {
            alert("Thu hồi quyền sử dụng thất bại")
            console.log("Error", error)
        })
    }
    $scope.initalize()
})