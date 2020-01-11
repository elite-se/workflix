// @flow

import React from 'react'
import { Button, Navbar } from '@blueprintjs/core'
import { Alignment } from '@blueprintjs/core/lib/cjs/common/alignment'
import { Link, Match } from '@reach/router'
import DarkModeSwitch from './modules/app/darkmode/DarkModeSwitch'

const QUARTER_HOUR = 900000
const THOUSAND = 1000

class Header extends React.Component<{ route: string }, { title: React$Node }> {
  state = { title: 'Workflix' }

  componentDidMount () {
    setTimeout(this.wontfix, QUARTER_HOUR)
  }

  wontfix = () => {
    this.setState(state => ({
      title: state.title === 'Workflix' ? <span style={{ fontSize: '1.03em' }}>Won'tfix</span> : 'Workflix'
    }))
    setTimeout(this.wontfix, THOUSAND * Math.random())
  }

  render () {
    const { route } = this.props
    return <Navbar>
      <Navbar.Group align={Alignment.LEFT}>
        <Navbar.Heading>{this.state.title}</Navbar.Heading>
        <Navbar.Divider onClick={this.wontfix}/>
        <Link to='tasks'><Button className='bp3-minimal' icon='form' text='Tasks overview' active={route === 'tasks'}/></Link>
        <Link to='processes'><Button className='bp3-minimal' icon='layers' text='Processes'
                                     active={route === 'processes'}/></Link>
        <Link to='process-templates'>
          <Button className='bp3-minimal' icon='gantt-chart' text='Process templates'
                  active={route === 'process-templates'}/></Link>
        <Link to='users'><Button className='bp3-minimal' icon='user' text='Users, Roles & Groups' active={route === 'users'}/></Link>
        <Link to='logout'><Button className='bp3-minimal' icon='lock' text='Logout'/></Link>
      </Navbar.Group>
      <Navbar.Group align={Alignment.RIGHT}>
        <DarkModeSwitch/>
      </Navbar.Group>
    </Navbar>
  }
}

const HeaderWithRouteName = () => <>
  <Match path='/tasks/*'>{props => props.match && <Header route='tasks'/>}</Match>
  <Match path='/processes/*'>{props => props.match && <Header route='processes'/>}</Match>
  <Match path='/process-templates/*'>{props => props.match && <Header route='process-templates'/>}</Match>
  <Match path='/users/*'>{props => props.match && <Header route='users'/>}</Match>
</>

export default HeaderWithRouteName
