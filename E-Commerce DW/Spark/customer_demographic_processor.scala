$ spark-shell

import scala.xml.XML

val customerRDD = sqlContext.read.parquet("/user/cloudera/bigretail/output/stores/sqoop/customers")

# customerRDD: org.apache.spark.sql.DataFrame = [CustomerID: int, AccountNumber: string, 
#     					         CustomerType: string, Demographics: string, 
#					         NameStyle: boolean, Title: string, FirstName: string, 
#					         MiddleName: string, LastName: string, Suffix: string, 
#					         EmailAddress: string, EmailPromotion: int, Phone: string, 
#					         AdditionalContactInfo: string, TerritoryID: int, territoryName: string, 
#					         countryregioncode: string, group: string, ModifiedDate: bigint]
#

val projDF = customerRDD.select("CustomerID","Demographics")

var kk = customerRDD.map(r => (r(0).toString.toInt,r))
kk.first
kk.reduceByKey()

# projDF.first
# res1: org.apache.spark.sql.Row = [11000,<IndividualSurvey xmlns="http://schemas.microsoft.com/sqlserver/2004/07/adventure-works/IndividualSurvey">
# <TotalPurchaseYTD>8248.99</TotalPurchaseYTD>
# <DateFirstPurchase>2001-07-22Z</DateFirstPurchase>
# <BirthDate>1966-04-08Z</BirthDate>
# <MaritalStatus>M</MaritalStatus>
# <YearlyIncome>75001-100000</YearlyIncome>
# <Gender>M</Gender>
# <TotalChildren>2</TotalChildren>
# <NumberChildrenAtHome>0</NumberChildrenAtHome>
# <Education>Bachelors </Education>
# <Occupation>Professional</Occupation>
# <HomeOwnerFlag>1</HomeOwnerFlag>
# <NumberCarsOwned>0</NumberCarsOwned>
# <CommuteDistance>1-2 Miles</CommuteDistance>
# </IndividualSurvey>]

case class CustomerDemo(CustomerID: Int, TotalPurchaseYTD: String, DateFirstPurchase : String, 
			BirthDate : String, MaritalStatus : String, YearlyIncome : String, Gender : String, 
			TotalChildren : String, NumberChildrenAtHome : String, Education : String, 
			Occupation : String, HomeOwnerFlag : String, NumberCarsOwned : String, 
			CommuteDistance : String) extends java.io.Serializable

case class CustomerDemo() extends java.io.Serializable


# defined class CustomerDemo

def createCustomerDemo(custId: Int,

def createCustDemo(custId: Int, demo:String) : CustomerDemo = {
	val node = XML.loadString(demo)
	CustomerDemo(
		custId,
		(node \\ "IndividualSurvey" \\ "TotalPurchaseYTD").text,
		(node \\ "IndividualSurvey" \\ "DateFirstPurchase").text,
		(node \\ "IndividualSurvey" \\ "BirthDate").text,
		(node \\ "IndividualSurvey" \\ "MaritalStatus").text,
		(node \\ "IndividualSurvey" \\ "YearlyIncome").text,
		(node \\ "IndividualSurvey" \\ "Gender").text,
		(node \\ "IndividualSurvey" \\ "TotalChildren").text,
		(node \\ "IndividualSurvey" \\ "NumberChildrenAtHome").text,
		(node \\ "IndividualSurvey" \\ "Education").text,
		(node \\ "IndividualSurvey" \\ "Occupation").text,
		(node \\ "IndividualSurvey" \\ "HomeOwnerFlag").text,
		(node \\ "IndividualSurvey" \\ "NumberCarsOwned").text,
		(node \\ "IndividualSurvey" \\ "CommuteDistance").text
	)
}
val projRDD = projDF.map(r => createCustDemo(r.get(0).toString.toInt, r.get(1).toString))

projRDD.toDF.write.format("parquet").mode("append").save("/user/cloudera/bigretail/output/stores/spark/customer_demographics")


