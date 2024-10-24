package com.example.softengineeringmessanger.di

import android.app.Application
import com.example.softengineeringmessanger.ui.auth.LoginFragment
import com.example.softengineeringmessanger.ui.auth.LoginViewModel
import com.example.softengineeringmessanger.ui.chat.ChatFragment
import com.example.softengineeringmessanger.ui.chat.ChatViewModel
import com.example.softengineeringmessanger.ui.chats.ChatListFragment
import com.example.softengineeringmessanger.ui.chats.ChatsListViewModel
import com.example.softengineeringmessanger.ui.contacts.ContactsFragment
import com.example.softengineeringmessanger.ui.contacts.ContactsViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class,
        UserRepositoryModule::class,
        SharedPreferencesModule::class,
        DatabaseModule::class,
        ChatRepositoryModule::class
    ]
)
interface AppComponent {
    fun inject(loginViewModel: LoginViewModel)
    fun inject(contactsViewModel: ContactsViewModel)
    fun inject(loginFragment: LoginFragment)
    fun inject(contactsFragment: ContactsFragment)
    fun inject(chatViewModel: ChatViewModel)
    fun inject(chatFragment: ChatFragment)
    fun inject(chatsListFragment: ChatListFragment)
    fun inject(chatListViewModel: ChatsListViewModel)
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}