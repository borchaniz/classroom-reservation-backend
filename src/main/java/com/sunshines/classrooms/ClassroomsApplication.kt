package com.sunshines.classrooms

import com.sunshines.classrooms.Service.StorageService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

import javax.annotation.Resource

@SpringBootApplication
class ClassroomsApplication : CommandLineRunner {

    @Resource
    internal var storageService: StorageService? = null

    @Throws(Exception::class)
    override fun run(vararg arg: String) {
        //        storageService.init();
    }

    companion object {


        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(ClassroomsApplication::class.java, *args)
        }
    }
}
