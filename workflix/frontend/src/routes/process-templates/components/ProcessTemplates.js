// @flow

import React from 'react'
import { Button } from '@blueprintjs/core'
import type { ProcessTemplateType } from '../../../modules/datatypes/Process'
import withPromiseResolver from '../../../modules/app/hocs/withPromiseResolver'
import ProcessApi from '../../../modules/api/ProcessApi'
import type { UserType } from '../../../modules/datatypes/User'
import UsersApi from '../../../modules/api/UsersApi'
import ProcessTemplateCard from './ProcessTemplateCard'
import styled from 'styled-components'
import FlipMove from 'react-flip-move'
import ProcessGroupsApi from '../../../modules/api/ProcessGroupsApi'
import type { ProcessGroupType } from '../../../modules/datatypes/ProcessGroup'
import { Link } from '@reach/router'

const TemplatesContainer = styled<{}, {}, 'div'>(FlipMove)`
  margin: 10px 20px;
  display: flex;
  flex: 1;
  justify-content: center;
  align-items: start;
  flex-direction: row;
  max-width: 100%;
  flex-wrap: wrap;
`

type PropsType = {
  templates: ProcessTemplateType[], users: Map<string, UserType>, refresh: (soft: boolean) => void,
  processGroups: Map<number, ProcessGroupType>
}

const CustomLink = styled(Link)`
  flex: 1;
  align-self: flex-end;
`

class ProcessTemplates extends React.Component<PropsType> {
  refresh = () => this.props.refresh(true)

  render () {
    const { users, templates, processGroups } = this.props
    return <div style={{
      display: 'flex',
      flexDirection: 'column'
    }}>
      <CustomLink to='/process-templates/create'>
        <Button icon='plus' intent='success'>Create new template</Button>
      </CustomLink>
      <TemplatesContainer>
        {templates.map(template => <ProcessTemplateCard key={template.id} template={template} users={users}
                                                        refresh={this.refresh} process processGroups={processGroups}/>)}
      </TemplatesContainer>
    </div>
  }
}

export default withPromiseResolver<PropsType, PropsType>(
  (props, refresh) => Promise.all([
    new ProcessApi().getAllProcessTemplates()
      .then(templates => new ProcessApi().getProcessTemplates(templates
        .filter(template => !template.deleted)
        .map(template => template.id))
      ),
    new UsersApi().getUsers(),
    new ProcessGroupsApi().getProcessGroups()
  ]).then(([templates, users, processGroups]) => {
    return ({
      templates,
      users,
      refresh,
      processGroups
    })
  })
)(ProcessTemplates)
