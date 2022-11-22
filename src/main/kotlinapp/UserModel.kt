//
//  UserModel.kt
//  Veracoin
//
//  A user of the Veracoin App
//

package kotlinapp

data class UserModel(
    var id: Int, 
    var name:String, 
    var email: String,
    var password: String,
    var subject: String,
    var friends: IntArray,
    var balance: Int
)

fun UserModel.isFriend(userId: Int): Boolean {
    return this.friends.contains(userId)
}