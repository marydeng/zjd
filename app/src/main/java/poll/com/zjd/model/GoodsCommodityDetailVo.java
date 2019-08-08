package poll.com.zjd.model;

import java.util.List;

/**
 * 创建者:   marydeng
 * 创建时间: 2017/8/9
 * 文件描述： 商品类的商品具体信息类
 */
public class GoodsCommodityDetailVo {
    private String id;
    private String prodName;
    private String alias;
    private double publicPrice;
    private String prodNo;
    private String prodId;
    private int prodType;
    private String brandId;
    private String brandName;
    private String brandNo;
    private String catId;
    private String catName;
    private String firstCategoriesId;
    private String firstCategoriesNo;
    private String firstCategoriesName;
    private String catNo;
    private String keywords;
    private String desc;
    private String commodityCategoriesPath;
    private String title;
    private double salePrice;
    private int isFreeFreight;
    private int stockCount;
    private double save;
    private double discount;
    private String scoreAvg;
    private int commentCount;
    private int favoriteCount;
    private String supplierId;
    private String supplierName;
    private String styleNo;
    private String specValueNo;
    private String specValueName;
    private CommodityPicVo commodityPicVo;
    private List<PropItem> lstPropItem;
    private List<LstGoods> lstGoods;
    private List<DefaultGroupPic> defaultGroupPic;
    private String lstCatb2c;
    private BasePromotionActiveVO basePromotionActiveVO;

    private List<ExtendPropList> extendPropList;
    private int activeType;
    private String proposal;
    private boolean flag;
    private String prodPropDesc;
    private int commodityStatus;

    public interface CommodityStatus {
        int on_shelves = 1;
        int offf_shelves = 2;
    }

    private String cardNo;
    private String cardNoNum;
    private String commodityNo;
    private String commodityName;
    private String commodityId;
    private String commodityPicSmall;
    private int salesCount;
    private int integral;
    private String commodityGuarantee;
    private String subcompanyId;

    public GoodsCommodityDetailVo() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public double getPublicPrice() {
        return publicPrice;
    }

    public void setPublicPrice(int publicPrice) {
        this.publicPrice = publicPrice;
    }

    public String getProdNo() {
        return prodNo;
    }

    public void setProdNo(String prodNo) {
        this.prodNo = prodNo;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public int getProdType() {
        return prodType;
    }

    public void setProdType(int prodType) {
        this.prodType = prodType;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandNo() {
        return brandNo;
    }

    public void setBrandNo(String brandNo) {
        this.brandNo = brandNo;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getFirstCategoriesId() {
        return firstCategoriesId;
    }

    public void setFirstCategoriesId(String firstCategoriesId) {
        this.firstCategoriesId = firstCategoriesId;
    }

    public String getFirstCategoriesNo() {
        return firstCategoriesNo;
    }

    public void setFirstCategoriesNo(String firstCategoriesNo) {
        this.firstCategoriesNo = firstCategoriesNo;
    }

    public String getFirstCategoriesName() {
        return firstCategoriesName;
    }

    public void setFirstCategoriesName(String firstCategoriesName) {
        this.firstCategoriesName = firstCategoriesName;
    }

    public String getCatNo() {
        return catNo;
    }

    public void setCatNo(String catNo) {
        this.catNo = catNo;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCommodityCategoriesPath() {
        return commodityCategoriesPath;
    }

    public void setCommodityCategoriesPath(String commodityCategoriesPath) {
        this.commodityCategoriesPath = commodityCategoriesPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public int getIsFreeFreight() {
        return isFreeFreight;
    }

    public void setIsFreeFreight(int isFreeFreight) {
        this.isFreeFreight = isFreeFreight;
    }

    public int getStockCount() {
        return stockCount;
    }

    public void setStockCount(int stockCount) {
        this.stockCount = stockCount;
    }

    public double getSave() {
        return save;
    }

    public void setSave(double save) {
        this.save = save;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getScoreAvg() {
        return scoreAvg;
    }

    public void setScoreAvg(String scoreAvg) {
        this.scoreAvg = scoreAvg;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getStyleNo() {
        return styleNo;
    }

    public void setStyleNo(String styleNo) {
        this.styleNo = styleNo;
    }

    public String getSpecValueNo() {
        return specValueNo;
    }

    public void setSpecValueNo(String specValueNo) {
        this.specValueNo = specValueNo;
    }

    public String getSpecValueName() {
        return specValueName;
    }

    public void setSpecValueName(String specValueName) {
        this.specValueName = specValueName;
    }

    public CommodityPicVo getCommodityPicVo() {
        return commodityPicVo;
    }

    public void setCommodityPicVo(CommodityPicVo commodityPicVo) {
        this.commodityPicVo = commodityPicVo;
    }

    public List<PropItem> getLstPropItem() {
        return lstPropItem;
    }

    public void setLstPropItem(List<PropItem> lstPropItem) {
        this.lstPropItem = lstPropItem;
    }

    public List<LstGoods> getLstGoods() {
        return lstGoods;
    }

    public void setLstGoods(List<LstGoods> lstGoods) {
        this.lstGoods = lstGoods;
    }

    public List<DefaultGroupPic> getDefaultGroupPic() {
        return defaultGroupPic;
    }

    public void setDefaultGroupPic(List<DefaultGroupPic> defaultGroupPic) {
        this.defaultGroupPic = defaultGroupPic;
    }

    public String getLstCatb2c() {
        return lstCatb2c;
    }

    public void setLstCatb2c(String lstCatb2c) {
        this.lstCatb2c = lstCatb2c;
    }

    public BasePromotionActiveVO getBasePromotionActiveVO() {
        return basePromotionActiveVO;
    }

    public void setBasePromotionActiveVO(BasePromotionActiveVO basePromotionActiveVO) {
        this.basePromotionActiveVO = basePromotionActiveVO;
    }

    public int getActiveType() {
        return activeType;
    }

    public void setActiveType(int activeType) {
        this.activeType = activeType;
    }

    public String getProposal() {
        return proposal;
    }

    public void setProposal(String proposal) {
        this.proposal = proposal;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getProdPropDesc() {
        return prodPropDesc;
    }

    public void setProdPropDesc(String prodPropDesc) {
        this.prodPropDesc = prodPropDesc;
    }

    public int getCommodityStatus() {
        return commodityStatus;
    }

    public void setCommodityStatus(int commodityStatus) {
        this.commodityStatus = commodityStatus;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardNoNum() {
        return cardNoNum;
    }

    public void setCardNoNum(String cardNoNum) {
        this.cardNoNum = cardNoNum;
    }

    public String getCommodityNo() {
        return commodityNo;
    }

    public void setCommodityNo(String commodityNo) {
        this.commodityNo = commodityNo;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public String getCommodityPicSmall() {
        return commodityPicSmall;
    }

    public void setCommodityPicSmall(String commodityPicSmall) {
        this.commodityPicSmall = commodityPicSmall;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public String getCommodityGuarantee() {
        return commodityGuarantee;
    }

    public void setCommodityGuarantee(String commodityGuarantee) {
        this.commodityGuarantee = commodityGuarantee;
    }

    public String getSubcompanyId() {
        return subcompanyId;
    }

    public void setSubcompanyId(String subcompanyId) {
        this.subcompanyId = subcompanyId;
    }

    public List<ExtendPropList> getExtendPropList() {
        return extendPropList;
    }

    public void setExtendPropList(List<ExtendPropList> extendPropList) {
        this.extendPropList = extendPropList;
    }

    public int getSalesCount() {
        return salesCount;
    }

    public void setSalesCount(int salesCount) {
        this.salesCount = salesCount;
    }
}
