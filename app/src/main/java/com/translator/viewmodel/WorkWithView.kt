package com.translator.viewmodel
import com.translator.domain.base.View

interface WorkWithView {

    fun attachView(view: View)
    fun detachView(view: View)

}
