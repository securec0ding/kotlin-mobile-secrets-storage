//
//  UserManager.swift
//  Veracoin
//
//  Singleton that manages users and tracks login state
//

package kotlinapp

enum class APIState(val result: Boolean) {
    SUCCESS(true),
    FAILURE(false)
}

object UserManager {
    private var userDict = HashMap<Int, UserModel>()
    public var currentUser: UserModel? = null
    
    init {
        loadUsers()
    }
    
    private fun loadUsers() {
        val user100 = UserModel(100, "John Q. Public", "john@example.com", "password", "yW6evr", intArrayOf(101, 102), 76)
        userDict[user100.id] = user100
        
        val user101 = UserModel(101, "Jane Doe", "jane@example.com", "supersecret", "V3KhM1", intArrayOf(100, 102), 77)
        userDict[user101.id] = user101
        
        val user102 = UserModel(102, "Zahir Arora", "Zahir Arora", "password", "n3pjjM", intArrayOf(101), 78)
        userDict[user102.id] = user102
    }

    public fun login(email:String, password:String, call:(APIState)->Unit) {
        for ((_, user) in userDict) {
            if (user.email == email) {
                if (user.password == password) {
                    this.currentUser = user
                    call(APIState.SUCCESS)
                    return
                }
            }
        }

        call(APIState.FAILURE)
    }

    public fun loginUser(id: Int) {
        this.currentUser = null

        if (this.userDict.containsKey(id)) {
            this.currentUser = this.userDict[id]
        }
    }
    
    public fun verify(subject:String, call:(APIState)->Unit) {
        for ((_, user) in userDict) {
            if (user.subject == subject) {
                this.currentUser = user
                call(APIState.SUCCESS)
                return
            }
        }

        call(APIState.FAILURE)
    }

    public fun findUser(id: Int): UserModel? {
        return userDict[id]
    }    
    
}