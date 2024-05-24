package com.joguco.logiaastro.interfaces

import com.joguco.logiaastro.database.entities.UserEntity

interface OnUserClick {
    fun onUserClick(user: UserEntity)
}