/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.codelabs.navigation

import android.os.Bundle
import android.view.*
import android.view.View.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import appLogic.AppState
import com.example.android.codelabs.navigation.databinding.HomeFragmentBinding
import helpers.HelperFunctions
import helpers.HelperFunctions.hideKeyboard
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.android.synthetic.main.navigation_activity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import services.LessonService
import services.LoginService

/**
 * Fragment used to show how to navigate to another destination
 */
class HomeFragment : Fragment() {
    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        LoginService.onLoginSuccessful += { user -> ShowWelcomeMessage(user.FirstName) }
        LoginService.onLoginUnsuccessful += { error -> ShowLoginFailMessage(error) }

        //TODO STEP 5 - Set an OnClickListener, using Navigation.createNavigateOnClickListener()
        binding.loginButton.setOnClickListener {
            findNavController().navigate(R.id.flow_step_one_dest, null)
        }
        //TODO END STEP 5

        //TODO STEP 6 - Set NavOptions
        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }
        binding.navigateDestinationButton.setOnClickListener {
            findNavController().navigate(R.id.flow_step_one_dest, null, options)
        }
        //TODO END STEP 6

        //TODO STEP 7.2 - Update the OnClickListener to navigate using an action
        binding.navigateActionButton.setOnClickListener {
            findNavController().navigate(R.id.next_action, null)
        }
        //TODO END STEP 7.2

        loginButton?.setOnClickListener{
            AppState
            val loginService: LoginService = LoginService()
            val email:String = loginEmail?.text.toString()
            val password:String = loginPassword?.text.toString()

            lifecycleScope.launch(Dispatchers.Main) {
                binding.progressBar.visibility =  VISIBLE
                loginButton?.visibility = INVISIBLE
                hideKeyboard()
            }
            lifecycleScope.launch(Dispatchers.IO) {
                loginService.LoginUser(email, password)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    private fun ShowWelcomeMessage(firstName:String)
    {
        lifecycleScope.launch(Dispatchers.Main) {
            binding.progressBar.visibility =  INVISIBLE
            loginButton?.isVisible = true
        }

        lifecycleScope.launch(Dispatchers.IO) {
            val lessonService: LessonService = LessonService()
            lessonService.GetCurrentLesson(AppState.CurrentUser)
        }

        activity?.runOnUiThread {
            run {
                //binding.loginResult.text = "Welcome, $firstName"
                //binding.loginResult.visibility = VISIBLE

                val options = navOptions {
                    anim {
                        enter = R.anim.slide_in_right
                        exit = R.anim.slide_out_left
                        popEnter = R.anim.slide_in_left
                        popExit = R.anim.slide_out_right
                    }
                }
                findNavController().navigate(R.id.flow_step_one_dest, null, options)
            }
        }


    }

    private fun ShowLoginFailMessage(error:String)
    {
        lifecycleScope.launch(Dispatchers.Main) {
            progressBar.visibility =  INVISIBLE
            loginButton?.isVisible = true
        }
        activity?.runOnUiThread {
            run {
                binding.loginResult.text = "Could not log in. Error: $error"
                binding.loginResult.visibility = VISIBLE
            }
        }
    }
}
