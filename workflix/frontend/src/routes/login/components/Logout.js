// @flow

import React from 'react'
import LoginApi from '../../../modules/api/LoginApi'
import withPromiseResolver from '../../../modules/app/hocs/withPromiseResolver'
import { removeToken } from '../../../modules/common/tokenStorage'
import CenterScreen from '../../../modules/common/components/CenterScreen'
import { navigate } from '@reach/router'

class Logout extends React.Component<{ onLoggedInChanged: (boolean) => void}> {
  componentDidMount () {
    removeToken()
    this.props.onLoggedInChanged(false)
    navigate('/')
  }

  render () {
    return <CenterScreen>You were successfully logged out.</CenterScreen>
  }
}

const promiseCreator = () => new LoginApi().logout()

export default withPromiseResolver<*, {}>(promiseCreator)(Logout)
