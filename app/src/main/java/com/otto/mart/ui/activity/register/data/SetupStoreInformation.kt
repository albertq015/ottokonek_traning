package com.otto.mart.ui.activity.register.data

class SetupStoreInformation {

    fun getBussinessType(): MutableList<StoreInformation> {
        var bussinessList = mutableListOf<StoreInformation>()

        bussinessList.apply {
            add(StoreInformation("Health & Beauty", "16966", getCulinaryCategory()))
            add(StoreInformation("Culinary", "16967", getHealthAndBeautyCategory()))
            add(StoreInformation("Grocery Store", "16968", getGroceryStoreCategory()))
            add(StoreInformation("Retail Store", "16969", getRetailCategory()))
            add(StoreInformation("Accommodation Services", "16970", getAccommodatioCategory()))
            add(StoreInformation("Automotive & Transportation", "16971", getAutomotiveCategory()))
            add(StoreInformation("Handphone & Electronics", "16972", getElectronicsCategory()))
            add(StoreInformation("Lifestyle", "16973", getLifestyleCategory()))
            add(StoreInformation("Others", "16974", getOthersCategory()))
        }

        return bussinessList
    }

    private fun getHealthAndBeautyCategory(): MutableList<StoreCategory>? {
        var categoryList = mutableListOf<StoreCategory>()

        categoryList.apply {
            add(StoreCategory("Cafe (Coffee and Hangout)", "5812"))
            add(StoreCategory("National Cuisine", "5993"))
            add(StoreCategory("Juices and Drinks", "5813"))
            add(StoreCategory("Coffee Shop", "20106"))
            add(StoreCategory("International Cuisine", "20063"))
            add(StoreCategory("Noodles and Asian Cuisine", "20061"))
            add(StoreCategory("Bread, Cakes and Snacks", "5441"))
            add(StoreCategory("Catering", "5811"))
        }

        return categoryList
    }

    private fun getCulinaryCategory(): MutableList<StoreCategory>? {
        var categoryList = mutableListOf<StoreCategory>()

        categoryList.apply {
            add(StoreCategory("Cosmetics and Beauty Shop", "5977"))
            add(StoreCategory("Salon", "7230"))
            add(StoreCategory("Pharmacy and Drugstore", "5912"))
            add(StoreCategory("Barbershop", "20009"))
            add(StoreCategory("Health and Alternative Clinics", "8099"))
            add(StoreCategory("Optical and Medical Equipment Store", "8043"))
            add(StoreCategory("Massage and Spa", "7298"))
        }

        return categoryList
    }

    private fun getGroceryStoreCategory(): MutableList<StoreCategory>? {
        var categoryList = mutableListOf<StoreCategory>()

        categoryList.apply {
            add(StoreCategory("Grocery store", "5199"))
            add(StoreCategory("Minimarket (Modern and Local)", "20066"))
            add(StoreCategory("Distributor, Agent, Supplier", "20021"))
            add(StoreCategory("Vegetable, Fruit and Meat Shop", "5422"))
        }

        return categoryList
    }

    private fun getRetailCategory(): MutableList<StoreCategory>? {
        var categoryList = mutableListOf<StoreCategory>()

        categoryList.apply {
            add(StoreCategory("Souvenir and Merchandise Shops", "5947"))
            add(StoreCategory("Clothing Shop", "20102"))
            add(StoreCategory("Shoes and Bags Shop", "5661"))
            add(StoreCategory("Construction material and Ceramics Store", "5713"))
            add(StoreCategory("Home Appliances Store", "5722"))
            add(StoreCategory("Bookstore and Office Stationery", "5942"))
            add(StoreCategory("Tavern", "5662"))
        }

        return categoryList
    }

    private fun getAccommodatioCategory(): MutableList<StoreCategory>? {
        var categoryList = mutableListOf<StoreCategory>()

        categoryList.apply {
            add(StoreCategory("Air Conditioning and Home Improvement Services", "7699"))
            add(StoreCategory("Parking lot", "7623"))
            add(StoreCategory("Newspaper Agent / Loper", "5192"))
            add(StoreCategory("Travel Agency Agents", "20001"))
            add(StoreCategory("Boarding House and Small Lodging", "20050"))
            add(StoreCategory("Key Duplication", "20051"))
            add(StoreCategory("Delivery and Logistics Services", "20057"))
            add(StoreCategory("Photocopy and Printing", "5111"))
            add(StoreCategory("Laundry", "7210"))
            add(StoreCategory("Tailor", "5949"))
        }

        return categoryList
    }

    private fun getAutomotiveCategory(): MutableList<StoreCategory>? {
        var categoryList = mutableListOf<StoreCategory>()

        categoryList.apply {
            add(StoreCategory("Vehicle Rental", "7512"))
            add(StoreCategory("Retail Gasoline", "5983"))
            add(StoreCategory("Accessories and Modifications Shop", "20068"))
            add(StoreCategory("Car Wash", "20015"))
            add(StoreCategory("Auto Repair Shop and Service", "7538"))
            add(StoreCategory("Parts Store", "7531"))
        }

        return categoryList
    }

    private fun getElectronicsCategory(): MutableList<StoreCategory>? {
        var categoryList = mutableListOf<StoreCategory>()

        categoryList.apply {
            add(StoreCategory("Electric Shop", "5732"))
            add(StoreCategory("Phone Store", "4812"))
            add(StoreCategory("Phone Credit Store", "20103"))
            add(StoreCategory("Computer store", "7311"))
            add(StoreCategory("Electronic store", "5948"))
        }

        return categoryList
    }

    private fun getLifestyleCategory(): MutableList<StoreCategory>? {
        var categoryList = mutableListOf<StoreCategory>()

        categoryList.apply {
            add(StoreCategory("Photo Studio and Photography Shop", "5946"))
            add(StoreCategory("Pet Store", "5995"))
            add(StoreCategory("Rental and Internet Café", "7994"))
            add(StoreCategory("Accessories and Jewelry Store", "5944"))
            add(StoreCategory("Studio and Music Shop", "5733"))
            add(StoreCategory("Fitness Aerobics and Sports Shop", "20026"))
            add(StoreCategory("Plant and Flower Shop", "5992"))
            add(StoreCategory("Toy store", "5945"))
        }

        return categoryList
    }

    private fun getOthersCategory(): MutableList<StoreCategory>? {
        var categoryList = mutableListOf<StoreCategory>()

        categoryList.apply {
            add(StoreCategory("Others", "7392"))
        }

        return categoryList
    }

}