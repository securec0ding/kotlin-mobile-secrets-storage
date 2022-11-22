//
//  Router.kt
//  Veracoin
//
//  Singleton for Routing on mock phone
//

package kotlinapp

object Router {
    fun showFriend(id: Int) {
        val auser = UserManager.findUser(id)
        auser?.also { user ->
            gWebSocket?.let { ws -> 
                ws.send("V|F|${user.name}")
            }
        } ?: run {
            gWebSocket?.let { ws -> 
                ws.send("V|F|Not found")
            }
        }
    }

    fun showHome() {
        gWebSocket?.let { ws -> 
            ws.send("V|M")
        }
    }

    fun showLogin() {        
        gWebSocket?.let { ws -> 
            ws.send("V|L")
        }
    }

    fun showForbidden() {
        gWebSocket?.let { ws -> 
            ws.send("V|X")
        }
    }

    fun showWithdraws() {
        gWebSocket?.let { ws -> 
            ws.send("V|W")
        }
    }
}