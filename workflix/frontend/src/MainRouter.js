// @flow

import React from 'react'
import { Match, Redirect, Router } from '@reach/router'
import ProcessTemplates from './routes/process-templates/components/ProcessTemplates'
import TasksOverview from './routes/tasks/components/TasksOverview'
import Users from './routes/users/components/UserManagement'
import EditProcessTemplate from './routes/edit-process-template/components/EditProcessTemplate'
import CreateProcessTemplate from './routes/create-process-template/components/CreateProcessTemplate'
import Logout from './routes/login/components/Logout'
import ProcessesOverview from './routes/processes-overview/components/ProcessesOverview'
import ProcessDetails from './routes/process-details/components/ProcessDetails'

type PropsType<Params> = { match: null | {| ...$Exact<Params> |} }

const UsersMatch = (props: PropsType<{}>) => props.match ? <Users/> : null
const TasksOverviewMatch = (props: PropsType<{}>) => props.match ? <TasksOverview/> : null
const ProcessTemplatesMatch = (props: PropsType<{}>) => props.match ? <ProcessTemplates/> : null
const CreateProcessTemplateMatch = (props: PropsType<{}>) => props.match ? <CreateProcessTemplate/> : null
const EditProcessTemplateMatch = (props: PropsType<{ id: string }>) => props.match
  ? <EditProcessTemplate id={Number(props.match.id)}/> : null
const ProcessesOverviewMatch = (props: PropsType<{}>) => props.match ? <ProcessesOverview/> : null
const ProcessDetailsMatch = (props: PropsType<{ id: string }>) => props.match
  ? <ProcessDetails id={Number(props.match.id)}/> : null
const LogoutMatch = (onLoggedInChanged: boolean => void) =>
  (props: PropsType<{}>) => props.match ? <Logout onLoggedInChanged={onLoggedInChanged}/> : null

class MainRouter extends React.Component<{ onLoggedInChanged: (boolean) => void}> {
  render () {
    return <>
      <Match path='/tasks'>{TasksOverviewMatch}</Match>
      <Match path='/users'>{UsersMatch}</Match>
      <Match path='/process-templates'>{ProcessTemplatesMatch}</Match>
      <Match path='/processes'>{ProcessesOverviewMatch}</Match>
      <Match path='/processes/:id'>{ProcessDetailsMatch}</Match>
      <Match path='/process-templates/create'>{CreateProcessTemplateMatch}</Match>
      <Match path='/process-templates/edit/:id'>{EditProcessTemplateMatch}</Match>
      <Match path='/logout'>{LogoutMatch(this.props.onLoggedInChanged)}</Match>
    </>
  }
}

export default (props: { onLoggedInChanged: (boolean) => void }) => (
  <Router>
    <Redirect from='/' to='/tasks' noThrow/>
    <MainRouter default onLoggedInChanged={props.onLoggedInChanged}/>
  </Router>
)
