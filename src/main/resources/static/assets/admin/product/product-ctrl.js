app.controller("product-ctrl", function ($scope, $http) {
    $scope.items = []
    $scope.cates = []
    $scope.form = {}

    $scope.initalize = function () {
        // Load products
        $http.get("/rest/products").then(resp => {
            $scope.items = resp.data;
            $scope.items.forEach(item => {
                item.createDate = new Date(item.createDate);
            })
        })
        // Load categories
        $http.get("/rest/categories").then(resp => {
            $scope.cates = resp.data;
        })
    }
    // Initialization
    $scope.initalize();

    // Reset form
    $scope.reset = function () {
        $scope.form = {
            createDate: new Date(),
            image: 'cloud-upload.jpg',
            available: true
        }
    }

    // Show form
    $scope.edit = function (item) {
        $scope.form = angular.copy(item)
        var triggerTabList = [].slice.call(document.querySelectorAll('#myTab button'))
        var triggerTab = triggerTabList[1] // Chọn tab thứ 2 (index 1)
        var tab = new bootstrap.Tab(triggerTab)
        tab.show()
    }

    // Add new product
    $scope.create = function () {
        var item = angular.copy($scope.form)
        $http.post(`/rest/products`, item).then(resp => {
            resp.data.createDate = new Date(resp.data.createDate)
            $scope.items.push(resp.data)
            $scope.reset()
            alert("Thêm mới sản phẩm thành công")
        }).catch(error => {
            alert("Lỗi thêm mới sản phẩm")
            console.log("Error", error)
        })
    }

    // Update product
    $scope.update = function () {
        var item = angular.copy($scope.form)
        $http.put(`/rest/products/${item.id}`, item).then(resp => {
            var index = $scope.items.findIndex(p => p.id == item.id)
            $scope.items[index] = item
            alert("Cập nhật thành công")
        }).catch(error => {
            alert("Lỗi cập nhật")
            console.log("Error", error)
        })
    }

    // Delete product
    $scope.delete = function (item) {
        $http.delete(`/rest/products/${item.id}`).then(resp => {
            var index = $scope.items.findIndex(p => p.id == item.id)
            $scope.items.splice(index, 1)
            $scope.reset()
            alert("Xóa thành công")
        }).catch(error => {
            alert("Lỗi xóa")
            console.log("Error", error)
        })
    }

    // Handle image upload
    $scope.imageChanged = function (files) {
        var data = new FormData();
        data.append('file', files[0]);
        $http.post('/rest/upload/images', data, {
            transformRequest: angular.identity,
            headers: { 'Content-Type': undefined }
        }).then(resp => {
            $scope.form.image = resp.data.name;
        }).catch(error => {
            alert("Lỗi upload hình ảnh");
            console.error("Error uploading image:", error);
        });
    }

    // Pagination
    $scope.pager = {
        page: 0,
        size: 10,
        get items() {
            var start = this.page * this.size
            return $scope.items.slice(start, start + this.size)
        },
        get pages() {
            return Math.ceil($scope.items.length / this.size);
        },
        setPage: function (page) {
            if (page >= 0 && page < this.pages) {
                this.page = page;
            }
        }
    }
});
