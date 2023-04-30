package com.example.myapplication

import com.google.gson.annotations.SerializedName


data class ChatGPTRespond (

    @SerializedName("id"      ) var id      : String?            = null,
    @SerializedName("object"  ) var obj : String?            = null,
    @SerializedName("created" ) var created : Int?               = null,
    @SerializedName("model"   ) var model   : String?            = null,
    @SerializedName("choices" ) var choices : ArrayList<Choices> = arrayListOf(),
    @SerializedName("usage"   ) var usage   : Usage?             = Usage()

)
data class Choices (

    @SerializedName("text"          ) var text         : String? = null,
    @SerializedName("index"         ) var index        : Int?    = null,
    @SerializedName("logprobs"      ) var logprobs     : String? = null,
    @SerializedName("finish_reason" ) var finishReason : String? = null

)
data class Usage (

    @SerializedName("prompt_tokens"     ) var promptTokens     : Int? = null,
    @SerializedName("completion_tokens" ) var completionTokens : Int? = null,
    @SerializedName("total_tokens"      ) var totalTokens      : Int? = null

)