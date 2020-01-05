// @flow

import React from 'react'
import { Card, H2, H3, Text } from '@blueprintjs/core'
import type { ProcessTemplateMasterDataType } from '../../../modules/datatypes/Process'
import withPromiseResolver from '../../../modules/app/hocs/withPromiseResolver'
import { Link } from '@reach/router'
import type { StyledComponent } from 'styled-components'
import styled from 'styled-components'
import ProcessApi from '../../../modules/api/ProcessApi'
import type { UserType } from '../../../modules/datatypes/User'
import UsersApi from '../../../modules/api/UsersApi'

const CustomLink: StyledComponent<{}, {}, *> = styled(Link)`
  margin: 5px;
`

type PropsType = { templates: ProcessTemplateMasterDataType[], users: Map<string, UserType> }

class ProcessTemplates extends React.Component<PropsType> {
  render () {
    const { users, templates } = this.props
    return <div style={{
      margin: '20px',
      display: 'flex',
      maxWidth: '300px',
      flex: '1',
      justifyContent: 'flex-start',
      alignItems: 'stretch',
      flexDirection: 'column'
    }}>
      <H2 style={{ textAlign: 'center' }}>All Process Templates</H2>
      {
        templates.map(template => {
            const owner = users.get(template.ownerId)
            return (
              <CustomLink to={`./edit/${template.id}`} key={template.id}><Card>
                <H3>{template.title}</H3>
                <Text>Erstellt von: <i>{owner?.name} ({owner?.displayname})</i></Text>
              </Card></CustomLink>)
          }
        )
      }
    </div>
  }
}

export default withPromiseResolver<PropsType, PropsType>(
  () => Promise.all([
    new ProcessApi().getAllProcessTemplates(),
    new UsersApi().getUsers()
  ]).then(([templates, users]) => {
    return ({
      templates,
      users
    })
  })
)(ProcessTemplates)
